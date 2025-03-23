package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Primary
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;
    private static final String CREATE_FILM_QUERY = "insert into films(name, description, release_date, duration, mpa) values (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa = ? where id = ?";
    private static final String SET_GENRE_QUERY = "insert into film_genre(film_id, genre_id) values(?, ?)";
    private static final String DELETE_GENRES_QUERY = "delete from film_genre where film_id = ?";

    private static final String BASE_DATA_QUERY = "select films.*, " +
            "mpa.name as mpa_name, " +
            "string_agg(genres.id, ', ') as genre_ids, " +
            "string_agg(genres.name, ', ') as genre_names, " +
            "(select count(user_id) from likes where film_id = films.id) as likes " +
            "from films " +
            "left join film_genre fg on fg.film_id = films.id " +
            "left join genres on genres.id = fg.genre_id " +
            "left join mpa on mpa.id = films.mpa";
    private static final String GET_BY_ID_FILM_QUERY = BASE_DATA_QUERY + " where films.id = ? group by films.id";
    private static final String GET_ALL_FILMS_QUERY = BASE_DATA_QUERY + " group by films.id";
    private static final String GET_POPULAR_FILMS_QUERY = BASE_DATA_QUERY + " group by films.id order by likes desc limit ?";
    private static final String GET_LIKES_QUERY = "select user_id from likes where film_id = ?";
    private static final String SET_LIKE_QUERY = "insert into likes(film_id, user_id) values(?, ?)";
    private static final String REMOVE_LIKE_QUERY = "delete from likes where film_id = ? and user_id = ?";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public Collection<Film> findAll() {
        return jdbc.query(GET_ALL_FILMS_QUERY, mapper);
    }

    @Override
    public Film create(Film film) {
        Long mpaId = getMpaId(film.getMpa());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_FILM_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, mpaId);
            return ps;
        }, keyHolder);

        Set<Long> genreIds = getGenreIds(film.getGenres());

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            film.setId(id);
            for (Long genreId : genreIds) {
                jdbc.update(SET_GENRE_QUERY, id, genreId);
            }
            return film;
        } else {
            throw new InternalServerException("Couldn't save data");
        }
    }

    @Override
    public Film update(Film newFilm) {
        Long mpaId = getMpaId(newFilm.getMpa());
        Set<Long> genreIds = getGenreIds(newFilm.getGenres());

        int rowsUpdated = jdbc.update(UPDATE_FILM_QUERY, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration(), mpaId, newFilm.getId());
        if (rowsUpdated > 0) {
            jdbc.update(DELETE_GENRES_QUERY, newFilm.getId());
            for (Long genreId : genreIds) {
                jdbc.update(SET_GENRE_QUERY, newFilm.getId(), genreId);
            }
            return newFilm;
        } else {
            throw new InternalServerException("Couldn't update data");
        }
    }

    @Override
    public Film findById(Long filmId) {
        return jdbc.queryForObject(GET_BY_ID_FILM_QUERY, mapper, filmId);
    }

    @Override
    public Set<Long> setLike(Long filmId, Long userId) {
        Set<Long> likes = getLikes(filmId);
        if (!likes.contains(userId)) {
            int rowsUpdated = jdbc.update(SET_LIKE_QUERY, filmId, userId);
            if (rowsUpdated > 0) {
                likes.add(userId);
            }
        }
        return likes;
    }

    @Override
    public Set<Long> deleteLike(Long filmId, Long userId) {
        jdbc.update(REMOVE_LIKE_QUERY, filmId, userId);
        return getLikes(filmId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return jdbc.query(GET_POPULAR_FILMS_QUERY, mapper, count);
    }

    public Set<Long> getLikes(Long filmId) {
        List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "films", List.of(filmId));
        if (checkVals.isEmpty()) {
            throw new NotFoundException("Couldn't find film with ID " + filmId);
        }
        List<Long> userIds = jdbc.queryForList(GET_LIKES_QUERY, Long.class, filmId);
        return new HashSet<>(userIds);
    }

    private Long getMpaId(Mpa mpa) {
        Long mpaId = (mpa != null) ? mpa.getId() : null;
        if (mpaId != null) {
            List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "mpa", List.of(mpaId));
            if (checkVals.isEmpty()) {
                throw new NotFoundException("Couldn't find mpa ratings by ID");
            }
        }
        return mpaId;
    }

    private Set<Long> getGenreIds(Set<Genre> genres) {
        Set<Long> genreIds = new HashSet<>();
        for (Genre genre : genres) {
            genreIds.add(genre.getId());
        }
        List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "genres", new ArrayList<>(genreIds));
        if (checkVals.size() != genreIds.size()) {
            throw new NotFoundException("Couldn't find genres by ID");
        }
        return genreIds;
    }
}
