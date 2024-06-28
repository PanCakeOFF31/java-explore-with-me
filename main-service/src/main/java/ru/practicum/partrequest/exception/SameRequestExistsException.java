package ru.practicum.partrequest.exception;


public class SameRequestExistsException extends RuntimeException {
    public SameRequestExistsException(String message) {
        super(message);
    }
}
