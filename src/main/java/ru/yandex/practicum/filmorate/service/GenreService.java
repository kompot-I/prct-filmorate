package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@RequiredArgsConstructor
@Service("genreService")
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre findById(Long genreId) {
        return genreStorage.findById(genreId);
    }

    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }
}
