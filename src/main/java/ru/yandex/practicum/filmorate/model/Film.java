package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.ReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "The name cannot be empty.")
    private String name;
    @Size(max = 200, message = "The maximum number of characters is 200.")
    private String description;
    @NotNull
    @ReleaseDate(message = "Release date cannot be earlier than December 28, 1895.")
    private LocalDate releaseDate;
    @Positive(message = "The duration of the movie must be a positive number.")
    private Integer duration;
    private final Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }
}