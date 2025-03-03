package ru.yandex.practicum.filmorate.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.utils.annotations.NullOrNotBlank;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {
    private boolean allowNull;

    @Override
    public void initialize(NullOrNotBlank constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (allowNull) {
            return s == null || !s.isBlank();
        } else {
            return s != null && !s.isBlank();
        }

    }
}
