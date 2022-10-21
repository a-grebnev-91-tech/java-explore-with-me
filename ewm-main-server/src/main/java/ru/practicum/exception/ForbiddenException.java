package ru.practicum.exception;

public class ForbiddenException extends BaseApiException {
    public ForbiddenException(String message, String reason) {
        super(message, reason);
    }
}
