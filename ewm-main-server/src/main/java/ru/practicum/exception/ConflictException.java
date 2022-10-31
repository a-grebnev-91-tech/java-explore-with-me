package ru.practicum.exception;

public class ConflictException extends BaseApiException {
    public ConflictException(String message, String reason) {
        super(message, reason);
    }
}
