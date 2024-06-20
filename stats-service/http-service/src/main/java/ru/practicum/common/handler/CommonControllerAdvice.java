package ru.practicum.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error.ErrorResponse;
import ru.practicum.exception.InvalidDurationException;
import ru.practicum.exception.MethodNotImplemented;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class CommonControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(MethodNotImplemented.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodNotImplemented(final MethodNotImplemented exception) {
        log.debug("{}- handleMethodNotImplemented", className);
        return new ErrorResponse("Ошибка выполнения endpoint",
                "Проблемы с реализацией endpoint, ", exception.getMessage());
    }

    @ExceptionHandler(InvalidDurationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDurationException(final InvalidDurationException exception) {
        log.debug("{}- handleInvalidDurationException", className);
        return new ErrorResponse("Ошибка параметров запроса",
                "Некорректные значение для промежутка выборки, ", exception.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParseException(final DateTimeParseException exception) {
        log.debug("{}- handleDateTimeParseException", className);
        return new ErrorResponse("Ошибка конвертации строки в LocalDateTime",
                "Некорректные значение строки, ", exception.getMessage());
    }
}
