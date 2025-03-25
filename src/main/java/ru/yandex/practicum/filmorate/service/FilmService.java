package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@Service("filmService")
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film findById(Long id) throws NotFoundException { //getItem
        return filmStorage.findById(id);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Set<Long> setLike(Long filmId, Long userId) throws NotFoundException {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("User with id = " + userId + " not found");
        }
        return filmStorage.setLike(filmId, userId);
    }

    public Set<Long> deleteLike(Long filmId, Long userId) throws NotFoundException {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("User with id " + userId + " doesn't exist");
        }
        return filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopular(count);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) throws NotFoundException {
        if (newFilm.getId() == null) {
            throw new ValidationException("ID is invalid");
        }
        return filmStorage.update(newFilm);
    }
}
