package ru.practicum.partrequest.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ApiError;
import ru.practicum.partrequest.exception.*;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, ассоциированные с сущностью - {@link ru.practicum.partrequest.model.ParticipationRequest}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiPartRequestControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(ParticipationLimitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationLimitException(ParticipationLimitException exception) {
        log.debug("{} - handleParticipationLimitException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ParticipationRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleParticipationRequestNotFoundException(ParticipationRequestNotFoundException exception) {
        log.debug("{} - handleParticipationRequestNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ParticRequestWithRequesterAndEventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleParticRequestWithRequesterAndEventNotFoundException(ParticRequestWithRequesterAndEventNotFoundException exception) {
        log.debug("{} - handleParticRequestWithRequesterAndEventNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(RequesterIsEventInitiatorException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequesterIsEventInitiatorException(RequesterIsEventInitiatorException exception) {
        log.debug("{} - handleRequesterIsEventInitiatorException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(SameRequestExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleSameRequestExistsException(SameRequestExistsException exception) {
        log.debug("{} - handleSameRequestExistsException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(WrongRequestStatusException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleWrongRequestStatusException(WrongRequestStatusException exception) {
        log.debug("{} - handleWrongRequestStatusException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
