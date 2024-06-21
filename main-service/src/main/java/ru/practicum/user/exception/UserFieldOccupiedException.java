package ru.practicum.user.exception;

public class UserFieldOccupiedException extends RuntimeException {
    public UserFieldOccupiedException(String message) {
        super(message);
    }
}