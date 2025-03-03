package ru.yandex.practicum.filmorate.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Repeatable(BirthdayDateOfCinemaValidator.List.class)
@Constraint(validatedBy = ru.yandex.practicum.filmorate.utils.validators.BirthdayDateOfCinemaValidator.class)
public @interface BirthdayDateOfCinemaValidator {
    String message() default "{CapitalLetter.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;

    @Target({FIELD})
    @Retention(RUNTIME)
    @interface List {
        BirthdayDateOfCinemaValidator[] value();
    }
}
