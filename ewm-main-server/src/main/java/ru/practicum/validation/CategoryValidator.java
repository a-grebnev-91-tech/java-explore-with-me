package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import ru.practicum.repository.CategoryRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class CategoryValidator implements ConstraintValidator<ExistingCategory, Long> {
    private final CategoryRepository repository;

    @Override
    public boolean isValid(Long categoryId, ConstraintValidatorContext constraintValidatorContext) {
        if (categoryId == null) return false;
        return repository.existsById(categoryId);
    }
}
