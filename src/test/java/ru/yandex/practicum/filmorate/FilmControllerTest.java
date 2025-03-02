package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {
    private Film film;
    private Set<ConstraintViolation<Film>> violations;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Disabled()
    @BeforeEach
    void setUp() {
        film = Film.builder()
                .id(1L)
                .description("Descr")
                .name("Snatch")
                .releaseDate(LocalDate.of(2001, 5, 10))
                .duration(1)
                .build();
    }

    @Disabled()
    @Test
    void blankNameTest() {
        film.setName("");
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Disabled()
    @Test
    void releaseDateTest() {
        film.setReleaseDate(LocalDate.of(1500, 1, 1));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Disabled()
    @Test
    void durationTest() {
        film.setDuration(-1);
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Disabled()
    @Test
    void addFilmTest() {
        violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }
}