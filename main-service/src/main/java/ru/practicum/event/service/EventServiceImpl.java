package ru.practicum.event.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.event.dto.*;
import ru.practicum.event.exception.*;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final LocationService locationService;
    private final CommonComponent commonComponent;
    private final EventRepository eventRepository;

    private static final String PUBLISHED_EVENTS_NOT_FOUND = "Published events with ids=%s was not found.";

    private static final String EVENT_WITH_INITIATOR_NOT_FOUND = "Event with id=%d and initiator id=%d was not found.";
    private static final String PUBLISHED_EVENT_WITH_INITIATOR_NOT_FOUND = "Published event with id=%d and initiator id=%d was not found.";
    private static final String INVALID_ADMIN_UPDATE_EVENT_DATE = "Дата начала изменяемого события должна быть не ранее чем за час от даты публикации.";
    private static final String INVALID_PRIVATE_CREATE_EVENT_DATE = "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s.";
    private static final String INVALID_PRIVATE_UPDATE_EVENT_DATE = "Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.";
    private static final String NOT_UPDATE_EVENT_DATE = "Нельзя обновлять событие которое уже началось или прошло.";
    private static final String NOT_PUBLISHED_EVENT_STATE = "Cannot publish the event because it's not in the right state: PUBLISHED.";
    private static final String NOT_PENDING_EVENT_STATE = "Cannot publish the event because it's not in the right state: PENDING.";
    private static final String NOT_PENDING_OR_CANCELED_EVENT_STATE = "Only pending or canceled events can be changed.";
    private static final String INVALID_UPDATE_PARTICIPANT_LIMIT = "Новый лимит меньше чем количество уже подтвержденных запросов.";

    @Override
    @Transactional
    public EventFullDto createPrivateEvent(long userId, NewEventDto newEventDto) throws InvalidEventDateException {
        log.debug("EventServiceImpl - service.createEvent({}, {})", userId, newEventDto);

        // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        eventDateValidation(LocalDateTime.now().plusHours(2),
                newEventDto.getEventDate(),
                String.format(INVALID_PRIVATE_CREATE_EVENT_DATE, newEventDto.getEventDate()));

        Event eventToCreate = prepareCreatePrivateEvent(userId, newEventDto);
        return EventMapper.mapToEventFullDto(eventRepository.save(eventToCreate));
    }

    private Event prepareCreatePrivateEvent(long userId, NewEventDto newEventDto) {
        Event eventToCreate = EventMapper.mapToEvent(newEventDto);
        eventToCreate.setCreatedOn(LocalDateTime.now());
        eventToCreate.setEventDate(newEventDto.getEventDate());

        User initiator = commonComponent.getUserById(userId);
        eventToCreate.setInitiator(initiator);

        Location location = locationService.getOrElseCreateLocation(newEventDto.getLocation());
        eventToCreate.setLocation(location);

        Category category = commonComponent.getCategoryById(newEventDto.getCategory());
        eventToCreate.setCategory(category);

        eventToCreate.setState(EventState.PENDING);
        eventToCreate.setConfirmedRequests(0);
        eventToCreate.setViews(0L);
        eventToCreate.setLikes(0L);
        eventToCreate.setDislikes(0L);
        eventToCreate.setRating(0F);

        return eventToCreate;
    }

    @Override
    public List<EventShortDto> fetchPrivateEventByUser(int from, int size, long userId) {
        log.debug("EventServiceImpl - service.fetchPrivateEventByUser({}, {}, {})", from, size, userId);

        commonComponent.userExists(userId);
        Pageable pageable = commonComponent.definePageable(from, size);

        return EventMapper.mapToEventShortDto(eventRepository.findAllByInitiatorId(userId, pageable));
    }

    @Override
    public EventFullDto fetchPrivateFullEventByUser(long userId, long eventId) throws EventWithInitiatorNotFoundException {
        log.debug("EventServiceImpl - service.fetchPrivateFullEventByUser(userId={}, eventId={})", userId, eventId);
        return EventMapper.mapToEventFullDto(getEventByIdAndInitiatorId(eventId, userId));
    }

    private Event getEventByIdAndInitiatorId(long eventId, long initiatorId) throws EventWithInitiatorNotFoundException {
        log.debug("CommonComponentImpl - component.getEventByIdAndInitiatorId(eventId={}, initiatorId={})", eventId, initiatorId);

        commonComponent.userExists(initiatorId);
        commonComponent.eventExists(eventId);

        return eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> {
                    String message = commonComponent
                            .prepareMessage(EVENT_WITH_INITIATOR_NOT_FOUND, eventId, initiatorId);
                    log.info(message);
                    return new EventWithInitiatorNotFoundException(message);
                });
    }

    @Override
    @Transactional
    public EventFullDto updatePrivateEvent(long userId, long eventId, UpdateEventUserRequest updateRequest) throws
            EventWithInitiatorNotFoundException,
            InvalidEventDateException,
            WrongEventStateException,
            InvalidParticipantLimitException {
        log.debug("EventServiceImpl - service.updatePrivateEvent(userId={}, eventId={}, {})", userId, eventId, updateRequest);

        Event updateEvent = prepareUpdatePrivateEvent(updateRequest);
        Event currentEvent = getEventByIdAndInitiatorId(eventId, userId);

        Location oldLocation = currentEvent.getLocation();
        Location updateLocation = updateEvent.getLocation();

        this.toUpdatePrivateValidation(currentEvent, updateEvent, updateRequest.getStateAction());
        Event eventToUpdate = preparePrivateEventToUpdate(currentEvent, updateEvent, updateRequest);

        eventToUpdate.setPublishedOn(null);
        EventFullDto result = EventMapper.mapToEventFullDto(eventRepository.save(eventToUpdate));

        if (updateLocation != null && !updateLocation.equals(oldLocation))
            locationService.deleteIfUnusedLocation(oldLocation.getId());

        return result;
    }

    private Event prepareUpdatePrivateEvent(UpdateEventUserRequest updateRequest) {
        log.debug("EventServiceImpl - service.prepareUpdatePrivateEvent({})", updateRequest);

        Event updateEvent = EventMapper.mapToEvent(updateRequest);

        if (updateRequest.getCategory() != null) {
            Category updateCategory = commonComponent.getCategoryById(updateRequest.getCategory());
            updateEvent.setCategory(updateCategory);
        }

        if (updateRequest.getLocation() != null) {
            Location updateLocation = locationService.getOrElseCreateLocation(updateRequest.getLocation());
            locationService.deleteIfUnusedLocation(updateLocation.getId());
            updateEvent.setLocation(updateLocation);
        }

        return updateEvent;
    }

    private void toUpdatePrivateValidation(Event currentEvent, Event updateEvent, UserStateAction userStateAction) {
        log.debug("EventServiceImpl - service.toUpdatePrivateValidation({}, {}, {})", currentEvent, updateEvent, userStateAction);

        if (updateEvent.getEventDate() != null) {
            // Нельзя обновлять событие, которое началось или прошло
            eventDateValidation(LocalDateTime.now(),
                    currentEvent.getEventDate(),
                    NOT_UPDATE_EVENT_DATE);

            eventDateValidation(LocalDateTime.now().plusHours(2),
                    updateEvent.getEventDate(),
                    INVALID_PRIVATE_UPDATE_EVENT_DATE);
        }

        if (currentEvent.getState() == EventState.PUBLISHED)
            commonComponent.throwAndLog(() -> new WrongEventStateException(NOT_PENDING_OR_CANCELED_EVENT_STATE));

        if (updateEvent.getParticipantLimit() != null &&
                updateEvent.getParticipantLimit() != 0 &&
                updateEvent.getParticipantLimit() < currentEvent.getConfirmedRequests())
            commonComponent.throwAndLog(() -> new InvalidParticipantLimitException(INVALID_UPDATE_PARTICIPANT_LIMIT));
    }

    private Event preparePrivateEventToUpdate(Event currentEvent, Event updateEvent, UpdateEventUserRequest updateRequest) {
        log.debug("EventServiceImpl - service.preparePrivateEventToUpdate({}, {}, {})", currentEvent, updateEvent, updateRequest);

        Event eventToUpdate = currentEvent.toBuilder().build();

        if (updateEvent.getCategory() != null)
            eventToUpdate.setCategory(updateEvent.getCategory());

        if (updateEvent.getLocation() != null)
            eventToUpdate.setLocation(updateEvent.getLocation());

        if (updateEvent.getTitle() != null)
            eventToUpdate.setTitle(updateEvent.getTitle());

        if (updateEvent.getAnnotation() != null)
            eventToUpdate.setAnnotation(updateEvent.getAnnotation());

        if (updateEvent.getDescription() != null)
            eventToUpdate.setDescription(updateEvent.getDescription());

        if (updateEvent.getEventDate() != null)
            eventToUpdate.setEventDate(updateEvent.getEventDate());

        if (updateEvent.getRequestModeration() != null)
            eventToUpdate.setRequestModeration(updateEvent.getRequestModeration());

        if (updateEvent.getParticipantLimit() != null)
            eventToUpdate.setParticipantLimit(updateEvent.getParticipantLimit());

        if (updateEvent.getPaid() != null)
            eventToUpdate.setPaid(updateEvent.getPaid());

        if (updateRequest.getStateAction() == UserStateAction.SEND_TO_REVIEW) {
            eventToUpdate.setState(EventState.PENDING);
        }

        if (updateRequest.getStateAction() == UserStateAction.CANCEL_REVIEW) {
            eventToUpdate.setState(EventState.CANCELED);
        }

        return eventToUpdate;
    }

    @Override
    public List<EventFullDto> fetchAdminEvents(List<Long> users,
                                               List<EventState> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               int from,
                                               int size) {
        log.debug("EventServiceImpl - service.getAdminEvents(users={},\nstates={},\ncategories={},\n" +
                        "rangeStart={}, rangeEnd={}, from={}, size={})",
                users, states, categories, rangeStart, rangeEnd, from, size);

        commonComponent.dateTimeRangeValidation(rangeStart, rangeEnd);

        Predicate predicate =
                formulateAdminEventsPredicate(users, states, categories, rangeStart, rangeEnd);

        Pageable pageable =
                formulateAdminEventsPageable(from, size);

        List<Event> events = eventRepository.findAll(predicate, pageable).stream()
                .collect(Collectors.toList());

        return EventMapper.mapToEventFullDto(events);
    }

    private Predicate formulateAdminEventsPredicate(List<Long> users,
                                                    List<EventState> states,
                                                    List<Long> categories,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd) {
        log.debug("EventServiceImpl - service.formulatePublicEventsPredicate(users={},\nstates={},\ncategories={},\n" +
                        "rangeStart={}, rangeEnd={})",
                users, states, categories, rangeStart, rangeEnd);

        BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

        if (users != null)
            predicate = predicate.and(QEvent.event.initiator.id.in(users));

        if (states != null)
            predicate = predicate.and(QEvent.event.state.in(states));

        if (categories != null)
            predicate = predicate.and(QEvent.event.category.id.in(categories));

        if (rangeStart != null)
            predicate = predicate.and(QEvent.event.eventDate.goe(rangeStart));

        if (rangeEnd != null)
            predicate = predicate.and(QEvent.event.eventDate.loe(rangeEnd));

        return predicate;
    }

    private Pageable formulateAdminEventsPageable(int from, int size) {
        return commonComponent.definePageable(from, size, Sort.by(Sort.Order.asc("id")));
    }

    @Override
    public List<EventShortDto> fetchPublicEvents(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable,
                                                 EventSort sort,
                                                 int from,
                                                 int size,
                                                 HttpServletRequest servletRequest) {
        log.debug("EventServiceImpl - service.getPublicEvents(text={}, categories={}, paid={},\n" +
                        "rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        commonComponent.dateTimeRangeValidation(rangeStart, rangeEnd);

        Predicate predicate =
                formulatePublicEventsPredicate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        Pageable pageable =
                formulatePublicEventsPageable(from, size, sort);

        List<Event> events = eventRepository.findAll(predicate, pageable).stream()
                .collect(Collectors.toList());

        return EventMapper.mapToEventShortDto(events);
    }

    private Predicate formulatePublicEventsPredicate(String text,
                                                     List<Long> categories,
                                                     Boolean paid,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable) {
        log.debug("EventServiceImpl - service.formulatePublicEventsPredicate(text={}, categories={}, paid={},\n" +
                        "rangeStart={}, rangeEnd={}, onlyAvailable={})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable);

        BooleanExpression predicate = QEvent.event.state.eq(EventState.PUBLISHED);

        if (text != null && !text.isBlank())
            predicate = predicate.and((QEvent.event.annotation.containsIgnoreCase(text))
                    .or(QEvent.event.description.containsIgnoreCase(text)));

        if (categories != null)
            predicate = predicate.and(QEvent.event.category.id.in(categories));

        if (paid != null)
            predicate = predicate.and(QEvent.event.paid.eq(paid));

        if (rangeStart == null && rangeEnd == null)
            predicate = predicate.and(QEvent.event.eventDate.gt(LocalDateTime.now()));

        if (rangeStart != null)
            predicate = predicate.and(QEvent.event.eventDate.goe(rangeStart));

        if (rangeEnd != null)
            predicate = predicate.and(QEvent.event.eventDate.loe(rangeEnd));

        if (onlyAvailable != null && onlyAvailable) {
            predicate = predicate.and(
                    (QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit))
                            .or(QEvent.event.participantLimit.eq(0)));
        }

        return predicate;
    }

    private Pageable formulatePublicEventsPageable(int from, int size, EventSort sort) {
        log.debug("EventServiceImpl - service.formulatePublicEventsPageable({}, {}, {})", from, size, sort);

        if (sort == null)
            return commonComponent.definePageable(from, size, Sort.by(Sort.Order.asc("id")));

        switch (sort) {
            case EVENT_DATE:
                return commonComponent.definePageable(from, size, Sort.by(Sort.Order.desc("eventDate")));
            case VIEWS:
                return commonComponent.definePageable(from, size, Sort.by(Sort.Order.desc("views")));
            case LIKES_DESC:
                return commonComponent.definePageable(from, size, Sort.by(Sort.Order.desc("likes")));
            case DISLIKES_DESC:
                return commonComponent.definePageable(from, size, Sort.by(Sort.Order.desc("dislikes")));
            case RATING_DESC:
                return commonComponent.definePageable(from, size, Sort.by(Sort.Order.desc("rating")));
            default:
                return commonComponent.definePageable(from, size, Sort.by(Sort.Order.asc("id")));
        }
    }

    @Override
    public EventFullDto fetchPublicEventById(long eventId, HttpServletRequest servletRequest) throws
            EventNotFoundException {
        log.debug("EventServiceImpl - service.fetchEventById({})", eventId);
        Event publishedEvent = commonComponent.getPublishedEventById(eventId);
        return EventMapper.mapToEventFullDto(publishedEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateAdminEvent(long eventId, UpdateEventAdminRequest updateRequest) throws
            InvalidEventDateException,
            WrongEventStateException,
            InvalidParticipantLimitException {
        log.debug("EventServiceImpl - service.updateAdminEvent({}, {})", eventId, updateRequest);

        commonComponent.eventExists(eventId);

        Event updateEvent = prepareUpdateAdminEvent(updateRequest);
        Event currentEvent = commonComponent.getEventById(eventId);

        this.toUpdateAdminValidation(currentEvent, updateEvent, updateRequest.getStateAction());
        Event eventToUpdate = prepareAdminEventToUpdate(currentEvent, updateEvent, updateRequest);

        eventToUpdate.setPublishedOn(LocalDateTime.now());
        return EventMapper.mapToEventFullDto(eventRepository.save(eventToUpdate));
    }

    private Event prepareUpdateAdminEvent(UpdateEventAdminRequest updateRequest) {
        log.debug("EventServiceImpl - service.prepareUpdateAdminEvent({})", updateRequest);

        Event updateEvent = EventMapper.mapToEvent(updateRequest);

        if (updateRequest.getCategory() != null) {
            Category updateCategory = commonComponent.getCategoryById(updateRequest.getCategory());
            updateEvent.setCategory(updateCategory);
        }

        if (updateRequest.getLocation() != null) {
            Location updateLocation = locationService.getOrElseCreateLocation(updateRequest.getLocation());
            locationService.deleteIfUnusedLocation(updateLocation.getId());
            updateEvent.setLocation(updateLocation);
        }

        return updateEvent;
    }

    private void toUpdateAdminValidation(Event currentEvent, Event updateEvent, AdminStateAction adminStateAction) {
        log.debug("EventServiceImpl - service.toUpdateAdminValidation({}, {}, {})", currentEvent,
                updateEvent, adminStateAction);

        if (updateEvent.getEventDate() != null) {
            // Нельзя обновлять событие, которое началось или прошло
            eventDateValidation(LocalDateTime.now(),
                    currentEvent.getEventDate(),
                    NOT_UPDATE_EVENT_DATE);

            eventDateValidation(LocalDateTime.now().plusHours(1),
                    updateEvent.getEventDate(),
                    INVALID_ADMIN_UPDATE_EVENT_DATE);
        }

        if (adminStateAction == AdminStateAction.PUBLISH_EVENT) {
            if (currentEvent.getState() != EventState.PENDING)
                commonComponent.throwAndLog(() -> new WrongEventStateException(NOT_PUBLISHED_EVENT_STATE));
        }

        if (adminStateAction == AdminStateAction.REJECT_EVENT) {
            if (currentEvent.getState() != EventState.PENDING)
                commonComponent.throwAndLog(() -> new WrongEventStateException(NOT_PENDING_EVENT_STATE));
        }

        if (updateEvent.getParticipantLimit() != null &&
                updateEvent.getParticipantLimit() != 0 &&
                updateEvent.getParticipantLimit() < currentEvent.getConfirmedRequests())
            commonComponent.throwAndLog(() -> new InvalidParticipantLimitException(INVALID_UPDATE_PARTICIPANT_LIMIT));
    }

    private Event prepareAdminEventToUpdate(Event currentEvent, Event updateEvent, UpdateEventAdminRequest
            updateRequest) {
        log.debug("EventServiceImpl - service.prepareAdminEventToSave({}, {}, {})", currentEvent, updateEvent, updateRequest);

        Event eventToUpdate = currentEvent.toBuilder().build();

        if (updateEvent.getCategory() != null)
            eventToUpdate.setCategory(updateEvent.getCategory());

        if (updateEvent.getLocation() != null)
            eventToUpdate.setLocation(updateEvent.getLocation());

        if (updateEvent.getTitle() != null)
            eventToUpdate.setTitle(updateEvent.getTitle());

        if (updateEvent.getAnnotation() != null)
            eventToUpdate.setAnnotation(updateEvent.getAnnotation());

        if (updateEvent.getDescription() != null)
            eventToUpdate.setDescription(updateEvent.getDescription());

        if (updateEvent.getEventDate() != null)
            eventToUpdate.setEventDate(updateEvent.getEventDate());

        if (updateEvent.getRequestModeration() != null)
            eventToUpdate.setRequestModeration(updateEvent.getRequestModeration());

        if (updateEvent.getParticipantLimit() != null)
            eventToUpdate.setParticipantLimit(updateEvent.getParticipantLimit());

        if (updateEvent.getPaid() != null)
            eventToUpdate.setPaid(updateEvent.getPaid());

        if (updateRequest.getStateAction() == AdminStateAction.PUBLISH_EVENT)
            eventToUpdate.setState(EventState.PUBLISHED);

        if (updateRequest.getStateAction() == AdminStateAction.REJECT_EVENT)
            eventToUpdate.setState(EventState.CANCELED);

        return eventToUpdate;
    }

    private void eventDateValidation(LocalDateTime timePoint1, LocalDateTime timePoint2, String message) {
        log.debug("EventServiceImpl - service.eventDateValidation({}, {})", timePoint1, timePoint2);

        if (timePoint2.isBefore(timePoint1))
            commonComponent.throwAndLog(() -> new InvalidEventDateException(message));
    }

    @Override
    public Event getPublishedEventByIdAndInitiatorId(long eventId, long initiatorId) throws
            PublishedEventWithInitiatorNotFoundException {
        log.debug("CommonComponentImpl - component.getPublishedEventByIdAndInitiatorId(eventId={}, initiatorId={})", eventId, initiatorId);

        commonComponent.userExists(initiatorId);
        commonComponent.eventExists(eventId);

        return eventRepository.findByIdAndInitiatorIdAndState(eventId, initiatorId, EventState.PUBLISHED)
                .orElseThrow(() -> {
                    String message = commonComponent
                            .prepareMessage(PUBLISHED_EVENT_WITH_INITIATOR_NOT_FOUND, eventId, initiatorId);
                    log.info(message);
                    return new EventWithInitiatorNotFoundException(message);
                });
    }

    @Override
    public List<Event> getEventsByIds(Set<Long> ids) throws EventNotFoundException {
        log.debug("EventServiceImpl - service.getEventsByIds({})", ids);

        if (ids == null || ids.isEmpty())
            return new ArrayList<>();

        List<Event> events = eventRepository.findAllById(ids);

        if (ids.size() != events.size()) {
            Set<Long> gotIds = events.stream().map(Event::getId).collect(Collectors.toSet());

            Set<Long> difference = new HashSet<>(ids);
            difference.removeAll(gotIds);

            commonComponent.throwAndLog(() -> new EventNotFoundException(commonComponent
                    .prepareMessage(PUBLISHED_EVENTS_NOT_FOUND, difference.toString())));
        }

        return events;
    }

    @Override
    @Transactional
    public void updateEventConfirmedRequestsValue(long eventId, int confirmedRequests) {
        log.debug("EventServiceImpl - service.updateEventConfirmedRequestsValue(eventId={}, {})", eventId, confirmedRequests);
        eventRepository.updateEventConfirmedRequestsValue(eventId, confirmedRequests);
    }

    @Override
    public void incrementConfirmedRequestsValue(long eventId) {
        log.debug("EventServiceImpl - service.incrementConfirmedRequestsValue(eventId={})", eventId);
        eventRepository.incrementEventConfirmedRequestsValue(eventId);
    }

    @Override
    @Transactional
    public void decrementConfirmedRequestsValue(long eventId) {
        log.debug("EventServiceImpl - service.decrementConfirmedRequestsValue(eventId={})", eventId);
        eventRepository.decrementEventConfirmedRequestsValue(eventId);
    }

    @Override
    public void incrementEventLikesValue(long eventId) {
        log.debug("EventServiceImpl - service.incrementEventLikesValue(eventId={})", eventId);
        eventRepository.incrementEventLikesValue(eventId);
    }

    @Override
    public void decrementEventLikesValue(long eventId) {
        log.debug("EventServiceImpl - service.decrementEventLikesValue(eventId={})", eventId);
        eventRepository.decrementEventLikesValue(eventId);
    }

    @Override
    public void incrementEventDislikesValue(long eventId) {
        log.debug("EventServiceImpl - service.incrementEventDislikesValue(eventId={})", eventId);
        eventRepository.incrementEventDislikesValue(eventId);
    }

    @Override
    public void decrementEventDislikesValue(long eventId) {
        log.debug("EventServiceImpl - service.decrementEventDislikesValue(eventId={})", eventId);
        eventRepository.decrementEventDislikesValue(eventId);
    }

    @Override
    public void updateEventRatingValue(long eventId, float rating) {
        log.debug("EventServiceImpl - service.updateEventRatingValue(eventId={}, {})", eventId, rating);
        eventRepository.updateEventRatingValue(eventId, rating);
    }
}