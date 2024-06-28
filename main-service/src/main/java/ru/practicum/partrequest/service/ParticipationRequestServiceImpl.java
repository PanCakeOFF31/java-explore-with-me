package ru.practicum.partrequest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.event.exception.WrongEventStateException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.partrequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.partrequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.partrequest.dto.ParticipationRequestDto;
import ru.practicum.partrequest.dto.ParticipationRequestMapper;
import ru.practicum.partrequest.exception.*;
import ru.practicum.partrequest.model.ParticipationRequest;
import ru.practicum.partrequest.model.RequestStatus;
import ru.practicum.partrequest.model.UpdateStatus;
import ru.practicum.partrequest.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final CommonComponent commonComponent;
    private final EventService eventService;
    private final ParticipationRequestRepository requestRepository;

    private static final String REQUESTS_NOT_FOUND = "Requests with ids=%s was not found";
    private static final String REQUEST_WITH_ID_AND_REQUESTER_NOT_FOUND = "Request with  id=%d and requester.id=%d was not found";
    private static final String REQUESTER_IS_INITIATOR = "Requester with requester.id=%d is initiator.";
    private static final String SAME_REQUEST_EXISTS = "Participation request with event.id=%d and requester.id=%d is already exists.";
    private static final String PARTICIPATION_LIMIT_OVER = "The participant limit has been reached.";
    private static final String NOT_PUBLISHED_EVENT = "Not published event.";
    private static final String NOT_PENDING_REQUEST_STATE = "It's not in the right state: PENDING";
    private static final String IS_REJECTED_REQUEST_STATE = "It's not in the right state: NO REJECTED";

    @Override
    @Transactional
    public ParticipationRequestDto createPrivateRequest(long eventId, long requesterId) throws
            WrongEventStateException,
            RequesterIsEventInitiatorException,
            SameRequestExistsException,
            ParticipationLimitException {
        log.debug("ParticipationRequestServiceImpl - service.createPrivateRequest({}, {})", eventId, requesterId);

        Event event = commonComponent.getEventById(eventId);
        User requester = commonComponent.getUserById(requesterId);

        toCreatePrivateRequestValidation(event, requester);

        ParticipationRequest requestToSave = preparePrivateRequestToSave(event, requester);
        return ParticipationRequestMapper.mapToParticipationRequestDto(requestRepository.save(requestToSave));
    }

    private void toCreatePrivateRequestValidation(Event event, User requester) {
        log.debug("ParticipationRequestServiceImpl - service.toCreatePrivateRequestValidation({}, {})", event, requester);

        EventState eventState = event.getState();
        long eventId = event.getId();
        long initiatorId = event.getInitiator().getId();
        long requesterId = requester.getId();
        int participantLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();

        if (eventState != EventState.PUBLISHED)
            commonComponent.throwAndLog(() -> new WrongEventStateException(commonComponent
                    .prepareMessage(NOT_PUBLISHED_EVENT, eventId)));

        // Проверка на участие в своем же событии
        if (initiatorId == requesterId)
            commonComponent.throwAndLog(() -> new RequesterIsEventInitiatorException(commonComponent
                    .prepareMessage(REQUESTER_IS_INITIATOR, requesterId)));

        // Проверка на повторный запрос
        Optional<ParticipationRequest> request = requestRepository.findByRequesterIdAndEventId(requesterId, eventId);
        if (request.isPresent())
            commonComponent.throwAndLog(() -> new SameRequestExistsException(commonComponent
                    .prepareMessage(SAME_REQUEST_EXISTS, eventId, requesterId)));

        if (participantLimit != 0 && confirmedRequests == participantLimit)
            commonComponent.throwAndLog(() -> new ParticipationLimitException(PARTICIPATION_LIMIT_OVER));
    }

    private ParticipationRequest preparePrivateRequestToSave(Event event, User requester) {
        log.debug("ParticipationRequestServiceImpl - service.preparePrivateRequestToSave({}, {})", event, requester);

        ParticipationRequest requestToSave = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .requester(requester)
                .event(event)
                .build();

        boolean requestModeration = event.getRequestModeration();
        int participantLimit = event.getParticipantLimit();

        if (!requestModeration || participantLimit == 0) {
            requestToSave.setStatus(RequestStatus.CONFIRMED);
            eventService.incrementConfirmedRequestsValue(event.getId());
        } else {
            requestToSave.setStatus(RequestStatus.PENDING);
        }

        return requestToSave;
    }

    @Override
    public List<ParticipationRequestDto> getPrivateUserRequests(long userId) {
        log.debug("ParticipationRequestServiceImpl - service.getPrivateUserRequests({})", userId);
        commonComponent.userExists(userId);
        return ParticipationRequestMapper.mapToParticipationRequestDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelPrivateSelfRequest(long requesterId, long requestId) throws ParticRequestWithRequesterAndEventNotFoundException {
        log.debug("ParticipationRequestServiceImpl - service.cancelPrivateSelfRequest({}, {})", requesterId, requestId);

        ParticipationRequest requestToUpdate = getByIdAndRequesterId(requestId, requesterId);

        if (requestToUpdate.getStatus() == RequestStatus.CONFIRMED) {
            requestToUpdate.setStatus(RequestStatus.CANCELED);
            eventService.decrementConfirmedRequestsValue(requestToUpdate.getEvent().getId());
            return ParticipationRequestMapper.mapToParticipationRequestDto(requestRepository.saveAndFlush(requestToUpdate));
        }

        requestToUpdate.setStatus(RequestStatus.CANCELED);
        return ParticipationRequestMapper.mapToParticipationRequestDto(requestRepository.saveAndFlush(requestToUpdate));
    }

    @Override
    public List<ParticipationRequestDto> fetchPrivateEventParticRequests(long userId, long eventId) {
        log.debug("ParticipationRequestServiceImpl - service.fetchPrivateEventParticRequests({}, {})", userId, eventId);

        Event event = eventService.getPublishedEventByIdAndInitiatorId(eventId, userId);
        List<ParticipationRequest> requests = event.getRequests();

        return ParticipationRequestMapper.mapToParticipationRequestDto(requests);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult modifyPrivateParticRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest) {
        log.debug("ParticipationRequestServiceImpl - service.modifyPrivateParticRequests({}, {}, {})", userId, eventId, updateRequest);

        Event event = eventService.getPublishedEventByIdAndInitiatorId(eventId, userId);
        List<ParticipationRequest> eventRequests = getParticRequestsByIds(event.getId(), updateRequest.getRequestIds());

        List<ParticipationRequest> requestsToUpdate =
                toModifyPrivateRequestsValidationAndProcess(event, eventRequests, updateRequest.getStatus());

        List<ParticipationRequest> savedRequests = requestRepository.saveAllAndFlush(requestsToUpdate);

        return prepareResponse(savedRequests);
    }

    private List<ParticipationRequest> getParticRequestsByIds(Long eventId, Set<Long> ids) throws
            ParticipationRequestNotFoundException {
        log.debug("ParticipationRequestServiceImpl - service.getParticRequestsByIds({})", ids);

        if (ids == null || ids.isEmpty())
            return new ArrayList<>();

        List<ParticipationRequest> events = requestRepository.findAllByEventIdAndIdIn(eventId, ids);

        if (ids.size() != events.size()) {
            Set<Long> gotIds = events.stream()
                    .map(ParticipationRequest::getId)
                    .collect(Collectors.toSet());

            Set<Long> difference = new HashSet<>(ids);
            difference.removeAll(gotIds);

            commonComponent.throwAndLog(() -> new ParticipationRequestNotFoundException(commonComponent
                    .prepareMessage(REQUESTS_NOT_FOUND, difference.toString())));
        }

        return events;
    }

    private List<ParticipationRequest> toModifyPrivateRequestsValidationAndProcess(Event event, List<ParticipationRequest> requests, UpdateStatus updateStatus) {
        log.debug("ParticipationRequestServiceImpl - service.toModifyPrivateRequestsValidationAndProcess({}, {}, {})", event, requests, updateStatus);

        List<ParticipationRequest> requestsToUpdate = new ArrayList<>(requests);

        final int participantLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();

        if (updateStatus == UpdateStatus.CONFIRMED) {

            if (participantLimit != 0 && participantLimit == confirmedRequests)
                commonComponent.throwAndLog(() -> new ParticipationLimitException(PARTICIPATION_LIMIT_OVER));

            for (ParticipationRequest request : requestsToUpdate) {
                if (request.getStatus() != RequestStatus.PENDING)
                    commonComponent.throwAndLog(() -> new WrongRequestStatusException(NOT_PENDING_REQUEST_STATE));

                if (participantLimit - confirmedRequests != 0) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(++confirmedRequests);
                } else
                    request.setStatus(RequestStatus.REJECTED);
            }
        }

        if (updateStatus == UpdateStatus.REJECTED) {
            for (ParticipationRequest request : requestsToUpdate) {
                if (request.getStatus() != RequestStatus.PENDING)
                    commonComponent.throwAndLog(() -> new WrongRequestStatusException(IS_REJECTED_REQUEST_STATE));

                request.setStatus(RequestStatus.REJECTED);
            }
        }

        eventService.updateEventConfirmedRequestsValue(event.getId(), confirmedRequests);

        return requestsToUpdate;
    }

    private EventRequestStatusUpdateResult prepareResponse(List<ParticipationRequest> savedRequests) {
        log.debug("ParticipationRequestServiceImpl - service.prepareResponse({})", savedRequests);

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        savedRequests.forEach(value -> {
            if (value.getStatus() == RequestStatus.CONFIRMED)
                confirmedRequests.add(ParticipationRequestMapper.mapToParticipationRequestDto(value));

            if (value.getStatus() == RequestStatus.REJECTED)
                rejectedRequests.add(ParticipationRequestMapper.mapToParticipationRequestDto(value));
        });

        return EventRequestStatusUpdateResult.of(confirmedRequests, rejectedRequests);
    }

    private ParticipationRequest getByIdAndRequesterId(long requestId, long requesterId) throws
            ParticRequestWithRequesterAndEventNotFoundException {
        log.debug("ParticipationRequestServiceImpl - service.getByIdAndRequesterId({}, {})", requesterId, requesterId);

        commonComponent.partrequestExists(requestId);
        commonComponent.userExists(requesterId);

        return requestRepository.findByIdAndRequesterId(requestId, requesterId)
                .orElseThrow(() -> {
                    String message = commonComponent
                            .prepareMessage(REQUEST_WITH_ID_AND_REQUESTER_NOT_FOUND, requestId, requesterId);
                    log.info(message);
                    return new ParticRequestWithRequesterAndEventNotFoundException(message);
                });
    }
}