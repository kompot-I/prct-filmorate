package ru.yandex.practicum.filmorate.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.utils.annotations.CustomEmail;

public class CustomEmailValidator implements ConstraintValidator<CustomEmail, String> {
    private boolean allowNull;

    @Override
    public void initialize(CustomEmail constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (allowNull) {
            return true;
        } else {
            return s != null;
        }
    }
}
