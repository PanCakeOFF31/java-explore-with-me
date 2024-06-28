package ru.practicum.rating.service;

import ru.practicum.rating.exception.InvalidRateDateException;
import ru.practicum.rating.exception.RatingNotFoundException;

public interface RatingService {
    void rateEvent(long userId, long eventId, int rate, boolean isTest) throws InvalidRateDateException;

    void deleteEventRate(long userId, long eventId) throws RatingNotFoundException;
}
