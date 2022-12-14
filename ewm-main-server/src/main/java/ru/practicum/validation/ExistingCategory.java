package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = CategoryIdValidator.class)
public @interface ExistingCategory {
    String message() default "Category id should exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
