package ru.practicum.exception;

import lombok.Getter;

public class ConflictException extends BaseApiException {
    public ConflictException(String message, String reason) {
        super(message, reason);
    }
}
