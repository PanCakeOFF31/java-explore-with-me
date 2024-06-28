package ru.practicum.location.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ApiError;
import ru.practicum.location.exception.LocationNotFoundException;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.NOT_FOUND_REASON;
import static ru.practicum.common.handler.ApiConstants.NOT_FOUND_STATUS;

/**
 * Обрабатывает исключения, ассоциированные с сущностью - {@link ru.practicum.location.model.Location}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiLocationControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleLocationNotFoundException(LocationNotFoundException exception) {
        log.debug("{} - handleLocationNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
