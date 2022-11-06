package ru.practicum.exception;

//TODO add handler
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, String reason) {
        super(message);
    }
}
