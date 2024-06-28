package ru.practicum.like.exception;

public class DislikeAlreadyExistsException extends RuntimeException {
    public DislikeAlreadyExistsException(String message) {
        super(message);
    }
}