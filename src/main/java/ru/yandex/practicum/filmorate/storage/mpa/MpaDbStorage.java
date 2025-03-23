package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.DatabaseUtils;

import java.util.Collection;
import java.util.List;

@Repository("mpaStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Mpa> mapper;

    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public Collection<Mpa> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Mpa create(Mpa user) {
        return null;
    }

    @Override
    public Mpa update(Mpa newUser) {
        return null;
    }

    @Override
    public Mpa findById(Long id) {
        List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "mpa", List.of(id));
        if (checkVals.isEmpty()) {
            throw new NotFoundException("Mpa rating with id = " + id + " not found");
        }

        return jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
    }
}
