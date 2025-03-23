package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FilmReleaseDateValidator.class)
public @interface FilmReleaseDate {
    String message() default "Date value out of approve date range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
