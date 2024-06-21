package ru.practicum.partrequest.exception;


public class WrongRequestStatusException extends RuntimeException {
    public WrongRequestStatusException(String message) {
        super(message);
    }
}
