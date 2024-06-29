package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import ru.practicum.StatClient;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.event.dto.*;
import ru.practicum.event.exception.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.stat.dto.StatRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Расширения поведения - ведение статистики за счет применения паттерна <b>декоратор</b>.
 */
@Slf4j
@Service
@Primary
public class EventServiceStatsImpl implements EventService {

    private final EventService eventService;
    private final StatClient statsHttpClient;
    private final EventRepository eventRepository;
    private final CommonComponent commonComponent;
    private final Queue<StatRequestDto> requestAccumulator;

    @Autowired
    public EventServiceStatsImpl(@Qualifier("eventServiceImpl") EventService eventService,
                                 StatClient statsHttpClient, EventRepository eventRepository,
                                 CommonComponent commonComponent) {
        this.eventService = eventService;
        this.statsHttpClient = statsHttpClient;
        this.eventRepository = eventRepository;
        this.commonComponent = commonComponent;
        this.requestAccumulator = new ArrayDeque<>();
    }

    @Override
    public EventFullDto createPrivateEvent(long userId, NewEventDto newEventDto) throws InvalidEventDateException {
        return eventService.createPrivateEvent(userId, newEventDto);
    }

    @Override
    public List<EventShortDto> fetchPrivateEventByUser(int from, int size, long userId) {
        return eventService.fetchPrivateEventByUser(from, size, userId);
    }

    @Override
    public EventFullDto fetchPrivateFullEventByUser(long userId, long eventId) throws EventWithInitiatorNotFoundException {
        return eventService.fetchPrivateFullEventByUser(userId, eventId);
    }

    @Override
    public EventFullDto updatePrivateEvent(long userId, long eventId, UpdateEventUserRequest updateRequest) throws EventWithInitiatorNotFoundException, InvalidEventDateException, WrongEventStateException, InvalidParticipantLimitException {
        return eventService.updatePrivateEvent(userId, eventId, updateRequest);
    }

    @Override
    public List<EventFullDto> fetchAdminEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return eventService.fetchAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @Override
    public List<EventShortDto> fetchPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, int from, int size, HttpServletRequest servletRequest) {
        List<EventShortDto> events = eventService.fetchPublicEvents(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size, servletRequest);

        // Механизм аккумуляции запросов, позволяет работать с отключенный StatsService
        try {
            processAccumulator(servletRequest);
            performHit(servletRequest);
            requestAccumulator.clear();
            return events;
        } catch (ResourceAccessException ignore) {
            log.warn("Connection to Stats server is refused when performHit(/events)");
            requestAccumulator.add(prepareRequest(servletRequest));
            return events;
        }
    }

    @Override
    public EventFullDto fetchPublicEventById(long eventId, HttpServletRequest servletRequest) throws EventNotFoundException {
        EventFullDto processedEvent = eventService.fetchPublicEventById(eventId, servletRequest);

        // Механизм аккумуляции запросов, позволяет работать с отключенный StatsService
        try {
            processAccumulator(servletRequest);

            Event publishedEvent = commonComponent.getPublishedEventById(eventId);
            publishedEvent.setViews(statsHttpClient.getUniqueEventViews(eventId));

            performHit(servletRequest);
            requestAccumulator.clear();
            return EventMapper.mapToEventFullDto(eventRepository.saveAndFlush(publishedEvent));
        } catch (ResourceAccessException exception) {
            log.warn("Connection to Stats server is refused performHit(/events/{})", eventId);
            requestAccumulator.add(prepareRequest(servletRequest));
            return processedEvent;
        }
    }

    private void processAccumulator(HttpServletRequest servletRequest) throws ResourceAccessException {
        Iterator<StatRequestDto> iterator = requestAccumulator.iterator();

        iterator.forEachRemaining(((request) -> {
            StatRequestDto first = requestAccumulator.peek();
            performHit(first);
            requestAccumulator.remove();
        }));
    }

    @Override
    public EventFullDto updateAdminEvent(long eventId, UpdateEventAdminRequest updateRequest) throws InvalidEventDateException, WrongEventStateException, InvalidParticipantLimitException {
        return eventService.updateAdminEvent(eventId, updateRequest);
    }

    @Override
    public Event getPublishedEventByIdAndInitiatorId(long eventId, long initiatorId) throws PublishedEventWithInitiatorNotFoundException {
        return eventService.getPublishedEventByIdAndInitiatorId(eventId, initiatorId);
    }

    @Override
    public List<Event> getEventsByIds(Set<Long> ids) throws EventNotFoundException {
        return eventService.getEventsByIds(ids);
    }

    @Override
    public void updateEventConfirmedRequestsValue(long eventId, int confirmedRequests) {
        eventService.updateEventConfirmedRequestsValue(eventId, confirmedRequests);
    }

    @Override
    public void incrementConfirmedRequestsValue(long eventId) {
        eventService.incrementConfirmedRequestsValue(eventId);
    }

    @Override
    public void decrementConfirmedRequestsValue(long eventId) {
        eventService.decrementConfirmedRequestsValue(eventId);
    }

    @Override
    public void incrementEventLikesValue(long eventId) {
        eventService.incrementEventLikesValue(eventId);
    }

    @Override
    public void decrementEventLikesValue(long eventId) {
        eventService.decrementEventLikesValue(eventId);
    }

    @Override
    public void incrementEventDislikesValue(long eventId) {
        eventService.incrementEventDislikesValue(eventId);
    }

    @Override
    public void decrementEventDislikesValue(long eventId) {
        eventService.decrementEventDislikesValue(eventId);
    }

    @Override
    public void updateEventRatingValue(long eventId, float rating) {
        eventService.updateEventRatingValue(eventId, rating);
    }

    private void performHit(HttpServletRequest servletRequest) {
        log.debug("EventServiceImpl - service.performHit()");
        statsHttpClient.toHit(prepareRequest(servletRequest));
    }

    private StatRequestDto prepareRequest(HttpServletRequest servletRequest) {
        log.debug("EventServiceImpl - service.prepareRequest()");
        String application = "ewm-main-service";
        return StatRequestDto.builder()
                .app(application)
                .uri(servletRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .ip(servletRequest.getRemoteAddr())
                .build();
    }

    private void performHit(StatRequestDto statRequestDto) {
        log.debug("EventServiceImpl - service.performHit({})", statRequestDto);
        statsHttpClient.toHit(statRequestDto);
    }
}
