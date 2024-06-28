package ru.practicum.category.exception;

public class CategoryFieldOccupiedException extends RuntimeException {
    public CategoryFieldOccupiedException(String message) {
        super(message);
    }
}
