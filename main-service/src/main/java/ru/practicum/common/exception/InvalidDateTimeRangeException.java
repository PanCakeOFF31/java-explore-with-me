package ru.practicum.common.exception;

public class InvalidDateTimeRangeException extends RuntimeException {
    public InvalidDateTimeRangeException(String message) {
        super(message);
    }
}
