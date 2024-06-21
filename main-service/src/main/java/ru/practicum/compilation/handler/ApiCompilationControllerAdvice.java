package ru.practicum.compilation.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ApiError;
import ru.practicum.compilation.exception.CompilationFieldOccupiedException;
import ru.practicum.compilation.exception.CompilationNotFoundException;

import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, ассоциированные с сущностью - {@link ru.practicum.compilation.dto}
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiCompilationControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(CompilationFieldOccupiedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCompilationFieldOccupiedException(CompilationFieldOccupiedException exception) {
        log.debug("{} - handleCompilationFieldOccupiedException", className);
        return ApiError.builder()
                .status(CONFLICT_STATUS)
                .reason(CONFLICT_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(CompilationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFoundException(CompilationNotFoundException exception) {
        log.debug("{} - handleCompilationNotFoundException", className);
        return ApiError.builder()
                .status(NOT_FOUND_STATUS)
                .reason(NOT_FOUND_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
