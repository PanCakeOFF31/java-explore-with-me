package ru.practicum.exception;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonControllerAdviceTest {
    private static CommonControllerAdvice commonControllerAdvice;

    @BeforeAll
    public static void initialize() {
        commonControllerAdvice = new CommonControllerAdvice();
    }

    @Test
    public void test_T0010_PS01_handleMethodNotImplemented() {
        ErrorResponse response = commonControllerAdvice.handleMethodNotImplemented(new MethodNotImplemented(""));
        assertEquals("Ошибка выполнения endpoint", response.getError());
        assertTrue(response.getDescription().startsWith("Проблемы с реализацией endpoint"));
    }

    @Test
    public void test_T0010_PS01_handleInvalidDurationException() {
        ErrorResponse response = commonControllerAdvice.handleInvalidDurationException(new InvalidDurationException(""));
        assertEquals("Ошибка параметров запроса", response.getError());
        assertTrue(response.getDescription().startsWith("Некорректные значение для промежутка выборки"));
    }

    @Test
    public void test_T0010_PS01_handleDateTimeParseException() {
        ErrorResponse response = commonControllerAdvice.handleDateTimeParseException(new DateTimeParseException("any", "any", 0));
        assertEquals("Ошибка конвертации строки в LocalDateTime", response.getError());
        assertTrue(response.getDescription().startsWith("Некорректные значение строки"));
    }
}