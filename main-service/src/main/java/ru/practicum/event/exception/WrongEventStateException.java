package ru.practicum.event.exception;

public class WrongEventStateException extends RuntimeException {
    public WrongEventStateException(String message) {
        super(message);
    }
}
