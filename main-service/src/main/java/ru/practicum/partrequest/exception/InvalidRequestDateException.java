package ru.practicum.partrequest.exception;


public class InvalidRequestDateException extends RuntimeException {
    public InvalidRequestDateException(String message) {
        super(message);
    }
}
