package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;


import java.time.LocalDate;


public class DateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value.isBefore(FIRST_RELEASE_DATE)) {
            throw new ValidationException("The release date is no earlier than December 28, 1895");
        }
        return true;
    }
}