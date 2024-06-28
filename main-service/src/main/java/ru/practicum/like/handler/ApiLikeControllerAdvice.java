package ru.practicum.like.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ApiError;
import ru.practicum.like.exception.*;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, ассоциированные с сущностью - {@link ru.practicum.like.model.Like}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiLikeControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(LikeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleLikeAlreadyExistsException(LikeAlreadyExistsException exception) {
        log.debug("{} - handleLikeAlreadyExistsException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(DislikeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDislikeAlreadyExistsException(DislikeAlreadyExistsException exception) {
        log.debug("{} - handleDislikeAlreadyExistsException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(LikeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleLikeNotFoundException(LikeNotFoundException exception) {
        log.debug("{} - handleLikeNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NoDislikeToUndoExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNoDislikeToUndoExistsException(NoDislikeToUndoExistsException exception) {
        log.debug("{} - handleNoDislikeToUndoExistsException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NoLikeToUndoExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNoLikeToUndoExistsException(NoLikeToUndoExistsException exception) {
        log.debug("{} - handleNoLikeToUndoExistsException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
