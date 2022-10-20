package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exception.ConflictException;
import ru.practicum.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository repo;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) return false;
        if (repo.findByEmail(email).isEmpty()) return true;
        else
            throw new ConflictException(
                    String.format("User with email %s already exist", email),
                    "Email should be unique"
            );
    }
}
