package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private User user;
    private UserController controller;
    private Set<ConstraintViolation<User>> violations;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void setUp() {
        controller = new UserController();
        user = User.builder()
                .id(1L)
                .name("name")
                .login("login")
                .email("ya@ya.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Test
    void addUserTest() {
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void incorrectEmailTest() {
        user.setEmail("email");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void blankLoginTest() {
        user.setLogin("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void loginNameIsSameTest() {
        user.setName("");
        controller.create(user);
        assertEquals("login", user.getName());
    }

    @Test
    void updateTest() {
        controller.create(user);
        user.setName("john");
        controller.update(user);
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}