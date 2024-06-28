package ru.practicum.partrequest.exception;


public class ParticipationLimitException extends RuntimeException {
    public ParticipationLimitException(String message) {
        super(message);
    }
}
