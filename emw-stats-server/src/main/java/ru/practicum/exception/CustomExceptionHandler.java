package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiError handleInternalError(Exception ex) {
        log.warn("Error occurred. Message: {}", ex.getMessage());
        return new ApiError(
                "Error occurred",
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );
    }
}
