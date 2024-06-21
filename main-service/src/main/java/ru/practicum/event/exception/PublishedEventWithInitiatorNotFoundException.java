package ru.practicum.event.exception;

public class PublishedEventWithInitiatorNotFoundException extends RuntimeException {
    public PublishedEventWithInitiatorNotFoundException(String message) {
        super(message);
    }
}
