package ru.practicum.event.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ApiError;
import ru.practicum.event.exception.*;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, ассоциированные с сущностью -  {@link ru.practicum.event.dto}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiEventControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventNotFoundException(EventNotFoundException exception) {
        log.debug("{} - handleEventNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(EventWithInitiatorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventWithInitiatorNotFoundException(EventWithInitiatorNotFoundException exception) {
        log.debug("{} - handleEventWithInitiatorNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidEventDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidEventDateException(InvalidEventDateException exception) {
        log.debug("{} - handleInvalidEventDateException", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidParticipantLimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidParticipantLimitException(InvalidParticipantLimitException exception) {
        log.debug("{} - handleInvalidParticipantLimitException", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(PublishedEventWithInitiatorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlePublishedEventWithInitiatorNotFoundException(PublishedEventWithInitiatorNotFoundException exception) {
        log.debug("{} - handlePublishedEventWithInitiatorNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(WrongEventStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleWrongEventStateException(WrongEventStateException exception) {
        log.debug("{} - handleWrongEventStateException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
