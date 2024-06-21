package ru.practicum.category.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.category.exception.CategoryFieldOccupiedException;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.common.error.ApiError;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, ассоциированные с сущностью - {@link ru.practicum.category.model.Category}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiCategoryControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(CategoryFieldOccupiedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryFieldOccupiedException(CategoryFieldOccupiedException exception) {
        log.debug("{} - handleCategoryFieldOccupiedException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFoundException(CategoryNotFoundException exception) {
        log.debug("{} - handleCategoryNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
