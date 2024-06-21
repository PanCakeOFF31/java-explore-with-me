package ru.practicum.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.common.error.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static ru.practicum.common.handler.ApiConstants.*;

/**
 * Обрабатывает исключения, связанные с валидацией запросов: тело, параметры, переменные
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ApiRequestControllerAdvice {
    private final String className = this.getClass().getName();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.debug("{} - - handleMethodArgumentNotValidException()", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.debug("{} - handleHttpMessageNotReadableException()", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.debug("{} - handleMethodArgumentTypeMismatchException()", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        log.debug("{} - handleMissingRequestHeaderException()", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.debug("{} - handleMissingServletRequestParameterException()", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    //     DefaultHandlerExceptionResolver перехватывает раньше - следует обдумать это
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiError handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.debug("{}- - handleHttpRequestMethodNotSupportedException()", className);
        return ApiError.builder()
                .status(METHOD_NOT_ALLOWED_STATUS)
                .reason(METHOD_NOT_ALLOWED_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(ConstraintViolationException exception) {
        log.debug("{} - handleConstraintViolationException()", className);
        return ApiError.builder()
                .status(BAD_REQUEST_STATUS)
                .reason(BAD_REQUEST_REASON)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}