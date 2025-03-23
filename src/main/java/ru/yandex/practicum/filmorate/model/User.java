package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    private Long id;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Incorrect date of birth.")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public User(String login, String email) {
        this.login = login;
        this.email = email;
    }
}