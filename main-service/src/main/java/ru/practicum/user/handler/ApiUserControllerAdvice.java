package ru.practicum.user.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ApiError;
import ru.practicum.user.exception.UserFieldOccupiedException;
import ru.practicum.user.exception.UserNotFoundException;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, ассоциированные с сущностью - {@link ru.practicum.user.model.User}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiUserControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(UserNotFoundException exception) {
        log.debug("{} - handleUserNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UserFieldOccupiedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserFieldOccupiedException(UserFieldOccupiedException exception) {
        log.debug("{} - handleUserFieldOccupiedException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
