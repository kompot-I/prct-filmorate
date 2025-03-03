package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.utils.Marker;
import ru.yandex.practicum.filmorate.utils.annotations.BirthdayDateOfCinemaValidator;
import ru.yandex.practicum.filmorate.utils.annotations.NullOrNotBlank;
import ru.yandex.practicum.filmorate.utils.annotations.PositiveDuration;
import ru.yandex.practicum.filmorate.utils.serde.DurationDeserializer;
import ru.yandex.practicum.filmorate.utils.serde.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    @NotNull(message = "The Id cannot be empty", groups = Marker.OnUpdate.class)
    Long id;

    @NullOrNotBlank(message = "The name must be specified", groups = Marker.OnCreate.class)
    @NullOrNotBlank(message = "The name cannot be empty.", groups = Marker.OnUpdate.class, allowNull = true)
    String name;

    @Size(max = 200, message = "The maximum number of characters is 200.")
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @BirthdayDateOfCinemaValidator(message = "The release date must be specified and be later than 12/28/1895", groups = Marker.OnCreate.class)
    @BirthdayDateOfCinemaValidator(message = "Release date cannot be earlier than 28/12/1895", groups = Marker.OnUpdate.class, allowNull = true)
    LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @PositiveDuration(message = "The duration of the film must be specified and be positive", groups = Marker.OnCreate.class)
    @PositiveDuration(message = "The length of the film should be positive", groups = Marker.OnUpdate.class, allowNull = true)
    Duration duration;

    Set<Long> likes = new HashSet<>();

    Set<Genre> genres = new LinkedHashSet<>();

    Rating mpa = new Rating();
}