package ru.practicum.event.exception;

public class InvalidParticipantLimitException extends RuntimeException {
    public InvalidParticipantLimitException(String message) {
        super(message);
    }
}
