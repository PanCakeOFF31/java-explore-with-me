package ru.practicum.rating.exception;


public class InvalidRateDateException extends RuntimeException {
    public InvalidRateDateException(String message) {
        super(message);
    }
}
