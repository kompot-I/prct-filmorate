package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;


import java.time.LocalDate;


public class DateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(FIRST_RELEASE_DATE);
    }
}