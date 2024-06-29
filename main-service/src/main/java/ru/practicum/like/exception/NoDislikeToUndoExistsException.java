package ru.practicum.like.exception;

public class NoDislikeToUndoExistsException extends RuntimeException {
    public NoDislikeToUndoExistsException(String message) {
        super(message);
    }
}