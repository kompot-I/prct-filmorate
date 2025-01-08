package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@Data
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "The name cannot be empty.")
    private String name;
    @Size(max = 200, message = "The maximum number of characters is 200.")
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "The duration of the movie must be a positive number.")
    private Integer duration;
    private final Set<Long> likes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }

    public Set<Long> getLikes() {
        return likes;
    }

    public int getLikesCount() {
        return likes.size();
    }
}