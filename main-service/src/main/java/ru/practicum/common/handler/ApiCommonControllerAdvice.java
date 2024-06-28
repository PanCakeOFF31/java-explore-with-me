package ru.practicum.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import ru.practicum.common.error.ApiError;
import ru.practicum.common.exception.InvalidDateTimeRangeException;
import ru.practicum.common.exception.MethodNotImplemented;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает общие исключения
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiCommonControllerAdvice {
    private final String className = this.getClass().getName();


    @ExceptionHandler(MethodNotImplemented.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ApiError handleMethodNotImplemented(MethodNotImplemented exception) {
        log.debug("{} - handleMethodNotImplemented", className);
        return ApiError.builder()
                .status(METHOD_NOT_IMPLEMENTED_STATUS)
                .reason(METHOD_NOT_IMPLEMENTED_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
        log.debug("{} - handleDataIntegrityViolationException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleResourceAccessException(final ResourceAccessException exception) {
        log.debug("{} - handleResourceAccessException", className);
        log.warn("Connection to Stats server is refused.");

        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidDateTimeRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidDateTimeRangeException(final InvalidDateTimeRangeException exception) {
        log.debug("{} - handleInvalidDateTimeRangeException", className);
        log.warn("Connection to Stats server is refused.");

        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
