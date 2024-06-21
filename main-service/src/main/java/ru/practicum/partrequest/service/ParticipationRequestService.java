package ru.practicum.partrequest.service;

import ru.practicum.event.exception.WrongEventStateException;
import ru.practicum.partrequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.partrequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.partrequest.dto.ParticipationRequestDto;
import ru.practicum.partrequest.exception.*;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto createPrivateRequest(long eventId, long userId) throws
            WrongEventStateException,
            RequesterIsEventInitiatorException,
            SameRequestExistsException,
            ParticipationLimitException;

    List<ParticipationRequestDto> getPrivateUserRequests(long userId);

    ParticipationRequestDto cancelPrivateSelfRequest(long requesterId, long requestId) throws ParticRequestWithRequesterAndEventNotFoundException;

    List<ParticipationRequestDto> fetchPrivateEventParticRequests(long userId, long eventId);

    EventRequestStatusUpdateResult modifyPrivateParticRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest) throws
            WrongRequestStatusException;
}
