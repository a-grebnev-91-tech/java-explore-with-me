package ru.practicum.exception;

import lombok.Getter;

public class ConflictException extends RuntimeException {
    @Getter
    private final String reason;
    public ConflictException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
