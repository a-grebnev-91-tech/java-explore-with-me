package ru.practicum.exception;

public class NotFoundException extends BaseApiException {
    public NotFoundException(String message, String reason) {
        super(message, reason);
    }
}
