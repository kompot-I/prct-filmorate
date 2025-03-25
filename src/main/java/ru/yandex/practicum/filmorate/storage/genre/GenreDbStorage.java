package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.DatabaseUtils;

import java.util.Collection;
import java.util.List;

@Repository("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapper;
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM genres";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public Collection<Genre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Genre create(Genre user) {
        return null;
    }

    @Override
    public Genre update(Genre newUser) {
        return null;
    }

    @Override
    public Genre findById(Long genreId) {
        List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "genres", List.of(genreId));
        if (checkVals.isEmpty()) {
            throw new NotFoundException("Genre with id = " + genreId + " not found");
        }
        return jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, genreId);
    }
}
