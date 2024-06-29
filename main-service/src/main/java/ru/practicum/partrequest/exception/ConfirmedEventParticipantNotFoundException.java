package ru.practicum.partrequest.exception;


public class ConfirmedEventParticipantNotFoundException extends RuntimeException {
    public ConfirmedEventParticipantNotFoundException(String message) {
        super(message);
    }
}
