package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private static long tempId = 0;
    private final HashMap<Long, Film> filmHashMap = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmHashMap.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(++tempId);
        filmHashMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + "добавлен");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!filmHashMap.containsKey(film.getId())) {
            log.debug("Такого фильма нет");
            throw new ValidationException("Такого фильма нет");
        }
        filmHashMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " обновлен");
        return film;
    }
}