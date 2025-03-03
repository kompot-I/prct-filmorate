package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.utils.Marker;
import ru.yandex.practicum.filmorate.utils.annotations.CustomEmail;
import ru.yandex.practicum.filmorate.utils.annotations.NullOrNotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @NotNull(message = "The ID must be specified", groups = Marker.OnUpdate.class)
    Long id;

    @CustomEmail(message = "An email cannot be empty. Also email must contain the @ symbol", groups = Marker.OnCreate.class)
    @CustomEmail(message = "The email must contain the @ symbol.", groups = Marker.OnUpdate.class, allowNull = true)
    String email;

    @NullOrNotBlank(message = "The login must not be empty.", groups = Marker.OnCreate.class)
    @NullOrNotBlank(message = "The login must not be empty.", groups = Marker.OnUpdate.class, allowNull = true)
    String login;

    String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Incorrect date of birth.")
    LocalDate birthday;

    final Set<Long> friends = new HashSet<>();
}