package ru.practicum.common.exception;

public class MethodNotImplemented extends RuntimeException {
    public MethodNotImplemented(String message) {
        super(message);
    }

    public MethodNotImplemented() {
        super();
    }
}
