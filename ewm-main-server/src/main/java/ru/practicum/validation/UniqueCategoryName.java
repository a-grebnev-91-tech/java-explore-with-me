package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = CategoryNameValidator.class)
public @interface UniqueCategoryName {
    String message() default "A category with such name already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
