package ru.practicum.exception;

public class ForbiddenOperationException extends BaseApiException {
    public ForbiddenOperationException(String message, String reason) {
        super(message, reason);
    }
}
