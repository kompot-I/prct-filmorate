package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        Film addFilm = filmService.addFilm(film);
        log.info("Film " + film.getName() + " added");
        return addFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updateFilm = filmService.update(film);
        log.info("Film " + film.getName() + " updated");
        return film;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable Long id) {
        filmService.deleteFilmById(id);
        log.info("The film was deleted");
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}