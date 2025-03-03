package ru.yandex.practicum.filmorate.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.utils.validators.PositiveDurationValidator;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Repeatable(PositiveDuration.List.class)
@Constraint(validatedBy = PositiveDurationValidator.class)
public @interface PositiveDuration {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;

    @Target({FIELD})
    @Retention(RUNTIME)
    @interface List {
        PositiveDuration[] value();
    }
}
