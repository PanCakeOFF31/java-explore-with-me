package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.like.service.LikeService;
import ru.practicum.rating.service.RatingService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateEventController {
    private final EventService eventService;
    private final LikeService likeService;
    private final RatingService ratingService;

    /**
     * <h5>Получение событий, добавленных текущим пользователем</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>в случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список</li>
     * </ul>
     */
    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> fetchPrivateEventsByUser(@RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                        @RequestParam(defaultValue = "10") @Positive final int size,
                                                        @PathVariable long userId) {
        log.debug("/users/{}/events - GET: fetchEventsByUser({}, {}, {})", userId, from, size, userId);
        return eventService.fetchPrivateEventByUser(from, size, userId);
    }

    /**
     * <h5>Добавление нового события</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента</li>
     * </ul>
     */
    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createPrivateEvent(@PathVariable long userId,
                                           @RequestBody @Valid NewEventDto newEventDto) {
        log.debug("/users/{}/events - POST: create({}, {})", userId, userId, newEventDto);
        return eventService.createPrivateEvent(userId, newEventDto);
    }

    /**
     * <h5>Получение полной информации о событии добавленном текущим пользователем</h5>
     * <ul>
     *     <li>в случае, если события с заданным id не найдено, возвращает статус код 404</li>
     * </ul>
     */
    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto fetchPrivateFullEventByUser(@PathVariable long userId,
                                                    @PathVariable long eventId) {
        log.debug("/users/{}/events/{} - GET: fetchPrivateFullEventByUser({}, {})", userId, eventId, userId, eventId);
        return eventService.fetchPrivateFullEventByUser(userId, eventId);
    }

    /**
     * <h5>Изменение события добавленного текущим пользователем</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)</li>
     *     <li>дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)</li>
     *     <li>Нельзя редактировать событие, которое началось или прошло</li>
     * </ul>
     */
    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updatePrivateEvent(@PathVariable long userId,
                                           @PathVariable long eventId,
                                           @RequestBody @Valid UpdateEventUserRequest updateRequest) {
        log.debug("/users/{}/events/{} - PATCH: updatePrivateEvent({}, {}, {})", userId, eventId,
                userId, eventId, updateRequest);
        return eventService.updatePrivateEvent(userId, eventId, updateRequest);
    }

    /**
     * <h5>Поставить лайк событию</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>Ставить лайк можно только опубликованному событию</li>
     *     <li>Владелец события может ставить лайк своему опубликованному событию</li>
     * </ul>
     */
    @PutMapping("/{userId}/events/{eventId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void likeEvent(@PathVariable long userId,
                          @PathVariable long eventId) {
        log.debug("/users/{}/events/{}/like - PUT: likeEvent(userId={}, eventId={})", userId, eventId, userId, eventId);
        likeService.likeEvent(userId, eventId);
    }

    /**
     * <h5>Убрать лайк события</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @DeleteMapping("/{userId}/events/{eventId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void undoLikeEvent(@PathVariable long userId,
                              @PathVariable long eventId) {
        log.debug("/users/{}/events/{}/like - DELETE: undoLikeEvent(userId={}, eventId={})", userId, eventId, userId, eventId);
        likeService.undoLikeEvent(userId, eventId);
    }

    /**
     * <h5>Поставить дизлайк событию</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>Ставить дизлайк можно только опубликованному событию</li>
     *     <li>Владелец события может ставить дизлайк своему опубликованному событию</li>
     * </ul>
     */
    @PutMapping("/{userId}/events/{eventId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public void dislikeEvent(@PathVariable long userId,
                             @PathVariable long eventId) {
        log.debug("/users/{}/events/{}/dislike - PUT: dislikeEvent(userId={}, eventId={})", userId, eventId, userId, eventId);
        likeService.dislikeEvent(userId, eventId);
    }

    /**
     * <h5>Убрать дизлайк события</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @DeleteMapping("/{userId}/events/{eventId}/dislike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void undoLDislikeEvent(@PathVariable long userId,
                                  @PathVariable long eventId) {
        log.debug("/users/{}/events/{}/dislike - DELETE: undoLikeEvent(userId={}, eventId={})", userId, eventId, userId, eventId);
        likeService.undoLDislikeEvent(userId, eventId);
    }

    /**
     * <h5>Оценить прошедшее событие или изменить поставленную оценку</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>Оценить можно событие только на следующий день всем участникам, чьи заявки были подтверждены</li>
     * </ul>
     */
    @PutMapping("/{userId}/events/{eventId}/rate")
    @ResponseStatus(HttpStatus.OK)
    public void rateEvent(@RequestParam @Min(0) @Max(10) int rate,
                          @PathVariable long userId,
                          @PathVariable long eventId) {
        log.debug("/users/{}/events/{}/rate - PUT: rateEvent(userId={}, eventId={}, {})", userId, eventId, userId, eventId, rate);
        ratingService.rateEvent(userId, eventId, rate, false);
    }

    /**
     * <h5>Удалить поставленную оценку</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li></li>
     * </ul>
     */
    @DeleteMapping("/{userId}/events/{eventId}/rate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRate(@PathVariable long userId,
                           @PathVariable long eventId) {
        log.debug("/users/{}/events/{}/rate - DELETE: deleteRate(userId={}, eventId={})", userId, eventId, userId, eventId);
        ratingService.deleteEventRate(userId, eventId);
    }
}