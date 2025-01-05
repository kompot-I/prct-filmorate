package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Электронная почта должна содержать символ @.")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым.")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Неверная дата рождения")
    private LocalDate birthday;
}