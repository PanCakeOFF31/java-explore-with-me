package ru.practicum.rating.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.partrequest.service.ParticipationRequestService;
import ru.practicum.rating.exception.InvalidRateDateException;
import ru.practicum.rating.exception.RatingNotFoundException;
import ru.practicum.rating.model.Rating;
import ru.practicum.rating.model.RatingId;
import ru.practicum.rating.repository.RatingRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {
    private final CommonComponent commonComponent;
    private final RatingRepository ratingRepository;
    private final EventService eventService;
    private final ParticipationRequestService requestService;

    private static final String NOT_RATE_EVENT = "Нельзя оценить событие раньше чем через сутки после начала.";

    @Override
    @Transactional
    public void rateEvent(long userId, long eventId, int rate, boolean isTest) throws InvalidRateDateException {
        log.debug("RatingServiceImpl - service.rateEvent({}, {}, {})", userId, eventId, rate);

        commonComponent.userExists(userId);
        Event event = commonComponent.getPublishedEventById(eventId);
        requestService.userIsConfirmedEventParticipant(userId, eventId);

        // Флаг тестового запроса
        if (!isTest)
            ratingDateValidation(event.getEventDate().plusDays(1), LocalDateTime.now(), NOT_RATE_EVENT);

        ratingRepository.save(Rating.of(userId, eventId, rate));

        float eventRatingAfterSave = ratingRepository.calculateEventRating(eventId).orElse(0.0F);
        eventRatingAfterSave = BigDecimal.valueOf(eventRatingAfterSave)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();

        eventService.updateEventRatingValue(eventId, eventRatingAfterSave);
    }

    @Override
    @Transactional
    public void deleteEventRate(long userId, long eventId) throws RatingNotFoundException {
        log.debug("RatingServiceImpl - service.deleteEventRate({}, {})", userId, eventId);

        commonComponent.userExists(userId);
        commonComponent.publishedEventExists(eventId);

        RatingId ratingId = RatingId.of(userId, eventId);
        commonComponent.ratingExists(ratingId);

        ratingRepository.deleteById(ratingId);

        float eventRatingAfterSave = ratingRepository.calculateEventRating(eventId).orElse(0.0F);
        eventRatingAfterSave = BigDecimal.valueOf(eventRatingAfterSave)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();

        eventService.updateEventRatingValue(eventId, eventRatingAfterSave);
    }

    private void ratingDateValidation(LocalDateTime timePoint1, LocalDateTime timePoint2, String message) {
        log.debug("RatingServiceImpl - service.requestDateValidation({}, {})", timePoint1, timePoint2);

        if (timePoint2.isBefore(timePoint1))
            commonComponent.throwAndLog(() -> new InvalidRateDateException(message));
    }
}
