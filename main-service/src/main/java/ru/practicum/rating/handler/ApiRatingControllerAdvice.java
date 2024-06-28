package ru.practicum.rating.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ApiError;
import ru.practicum.rating.exception.InvalidRateDateException;
import ru.practicum.rating.exception.RatingNotFoundException;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, ассоциированные с сущностью - {@link ru.practicum.rating.model.Rating}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiRatingControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(RatingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleRatingNotFoundException(RatingNotFoundException exception) {
        log.debug("{} - handleRatingNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidRateDateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidRateDateException(InvalidRateDateException exception) {
        log.debug("{} - handleInvalidRateDateException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}