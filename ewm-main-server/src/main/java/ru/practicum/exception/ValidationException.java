package ru.practicum.exception;

public class ValidationException extends BaseApiException {
    public ValidationException(String message, String reason) {
        super(message, reason);
    }
}
