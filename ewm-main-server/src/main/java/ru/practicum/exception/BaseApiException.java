package ru.practicum.exception;

import lombok.Getter;

public abstract class BaseApiException extends RuntimeException {
    @Getter
    private String reason;

    public BaseApiException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
