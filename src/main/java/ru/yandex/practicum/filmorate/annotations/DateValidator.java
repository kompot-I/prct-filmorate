package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        formatter.setLenient(false);
        if (value.isBefore(FIRST_RELEASE_DATE)) {
            return false;
        }
        try {
            formatter.parse(value.toString());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}