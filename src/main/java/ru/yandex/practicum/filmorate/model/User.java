package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
    @NotBlank(message = "An email cannot be empty.")
    @Email(message = "The email must contain the @ symbol.")
    private String email;
    @NotBlank(message = "The login must not be empty.")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Incorrect date of birth.")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}