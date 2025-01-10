package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final ValidationService validationService;

    @Autowired
    public FilmService(FilmStorage filmStorage, ValidationService validationService) {
        this.filmStorage = filmStorage;
        this.validationService = validationService;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public void addLike(Long filmId, Long userId) {
        validationService.validateUserExists(userId);
        Film film = validationService.validateFilmExists(filmId);
        film.addLike(userId);
    }

    public Film update(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilmById(Long id) {
        filmStorage.deleteFilm(id);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Film with id " + id + " not found"));
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found"));
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikesCount(), f1.getLikesCount()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
