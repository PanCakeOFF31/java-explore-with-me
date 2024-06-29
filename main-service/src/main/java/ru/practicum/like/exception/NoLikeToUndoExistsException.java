package ru.practicum.like.exception;

public class NoLikeToUndoExistsException extends RuntimeException {
    public NoLikeToUndoExistsException(String message) {
        super(message);
    }
}