package ru.practicum.rating.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.rating.service.RatingService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * <h5>Контроллер для тестирования рейтинга - не более</h5>
 * <p>Согласно философии проекта: допускается ставить рейтинг только тем пользователям, которые
 * участвовали в событии - статус на момент начала является CONFIRMED</p>
 * <p>Обратите внимание:</p>
 * <ul>
 *     <li>Дублирует пути и методы к ресурсам</li>
 *     <li>Запросы через обычный контроллер не позволяют выставления рейтинга</li>
 * </ul>
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/test/users")
public class TestRatingController {
    private final RatingService ratingService;

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
        log.debug("/test/users/{}/events/{}/rate - PUT: rateEvent(userId={}, eventId={}, {})", userId, eventId, userId, eventId, rate);
        ratingService.rateEvent(userId, eventId, rate, true);
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
