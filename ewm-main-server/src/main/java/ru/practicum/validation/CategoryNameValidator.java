package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exception.ConflictException;
import ru.practicum.repository.CategoryRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class CategoryNameValidator implements ConstraintValidator<UniqueCategoryName, String> {
    private final CategoryRepository repo;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name.isBlank()) return false;
        if (repo.existsByName(name)) {
            throw new ConflictException(
                    "Category name should be unique",
                    String.format("Category with name %s already exist", name)
            );
        } else {
            return true;
        }
    }
}
