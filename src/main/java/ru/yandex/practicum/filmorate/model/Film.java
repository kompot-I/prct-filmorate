package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.FilmReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200, message = "The maximum number of characters is 200.")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FilmReleaseDate
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    Set<Long> likes = new HashSet<>();

    Set<Genre> genres = new LinkedHashSet<>();

    Mpa mpa = new Mpa();
}