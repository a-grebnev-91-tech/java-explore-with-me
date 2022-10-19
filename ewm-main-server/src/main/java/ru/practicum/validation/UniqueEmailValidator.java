package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import ru.practicum.exception.ConflictEmailException;
import ru.practicum.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository repo;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) return false;
        if (repo.findByEmail(email).isPresent()) return true;
        else throw new ConflictEmailException("Email should be unique");
    }
}
