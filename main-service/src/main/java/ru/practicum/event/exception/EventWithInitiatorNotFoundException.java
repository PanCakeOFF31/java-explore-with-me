package ru.practicum.event.exception;

public class EventWithInitiatorNotFoundException extends RuntimeException {
    public EventWithInitiatorNotFoundException(String message) {
        super(message);
    }
}
