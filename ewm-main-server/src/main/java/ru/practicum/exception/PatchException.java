package ru.practicum.exception;

public class PatchException extends BaseApiException {
    public PatchException(String reason, String message) {
        super(reason, message);
    }
}
