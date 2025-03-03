package ru.yandex.practicum.filmorate.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Month;

public class BirthdayDateOfCinemaValidator implements ConstraintValidator<ru.yandex.practicum.filmorate.utils.annotations.BirthdayDateOfCinemaValidator, LocalDate> {

    private static final LocalDate birthdayOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);
    private boolean allowNull;

    @Override
    public void initialize(ru.yandex.practicum.filmorate.utils.annotations.BirthdayDateOfCinemaValidator constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (allowNull) {
            return localDate == null || localDate.isAfter(birthdayOfCinema);
        } else {
            return localDate != null && localDate.isAfter(birthdayOfCinema);
        }
    }
}
