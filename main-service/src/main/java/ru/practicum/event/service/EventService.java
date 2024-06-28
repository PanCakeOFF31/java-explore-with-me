package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.exception.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {

    EventFullDto createPrivateEvent(long userId, NewEventDto newEventDto) throws InvalidEventDateException;

    List<EventShortDto> fetchPrivateEventByUser(int from, int size, long userId);


    EventFullDto fetchPrivateFullEventByUser(long userId, long eventId) throws EventWithInitiatorNotFoundException;

    EventFullDto updatePrivateEvent(long userId, long eventId, UpdateEventUserRequest updateRequest) throws
            EventWithInitiatorNotFoundException,
            InvalidEventDateException,
            WrongEventStateException,
            InvalidParticipantLimitException;

    List<EventFullDto> fetchAdminEvents(List<Long> users,
                                        List<EventState> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size);

    List<EventShortDto> fetchPublicEvents(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          Boolean onlyAvailable,
                                          EventSort sort,
                                          int from,
                                          int size,
                                          HttpServletRequest servletRequest);

    EventFullDto fetchPublicEventById(long eventId,
                                      HttpServletRequest servletRequest) throws EventNotFoundException;


    EventFullDto updateAdminEvent(long eventId, UpdateEventAdminRequest updateRequest) throws
            InvalidEventDateException,
            WrongEventStateException,
            InvalidParticipantLimitException;

    Event getPublishedEventByIdAndInitiatorId(long eventId, long initiatorId) throws PublishedEventWithInitiatorNotFoundException;

    List<Event> getEventsByIds(Set<Long> ids) throws EventNotFoundException;

    void updateEventConfirmedRequestsValue(long eventId, int confirmedRequests);

    void incrementConfirmedRequestsValue(long eventId);

    void decrementConfirmedRequestsValue(long eventId);

    void incrementEventLikesValue(long eventId);

    void decrementEventLikesValue(long eventId);

    void incrementEventDislikesValue(long eventId);

    void decrementEventDislikesValue(long eventId);

    void updateEventRatingValue(long eventId, float rating);
}

