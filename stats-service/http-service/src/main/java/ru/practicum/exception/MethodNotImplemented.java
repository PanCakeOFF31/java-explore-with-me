package ru.practicum.exception;

public class MethodNotImplemented extends RuntimeException {
    public MethodNotImplemented(String message) {
        super(message);
    }

    public MethodNotImplemented() {
        super();
    }
}
