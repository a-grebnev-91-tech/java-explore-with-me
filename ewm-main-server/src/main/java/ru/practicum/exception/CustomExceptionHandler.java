package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ApiError handleConflict(ConflictException ex) {
        log.info("Conflict exception has occurred. Message: {}", ex.getMessage());
        return new ApiError(ex.getMessage(), ex.getReason(), HttpStatus.CONFLICT.getReasonPhrase().toUpperCase());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            ValidationException.class
    })
    public ApiError handleConstraintViolation(Exception ex) {
        log.info("Validation exception has occurred. Message: {}", ex.getMessage());
        return new ApiError(
                ex.getMessage(),
                "Validation failed",
                "BAD_REQUEST"
        );
    }
}
