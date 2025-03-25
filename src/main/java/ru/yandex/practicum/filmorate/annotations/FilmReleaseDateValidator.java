package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Month;

public class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDate, LocalDate> {

    private LocalDate fromReleaseDate;

    @Override
    public void initialize(FilmReleaseDate constraintAnnotation) {
        fromReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate == null || localDate.isEqual(fromReleaseDate) || localDate.isAfter(fromReleaseDate);
    }
}
