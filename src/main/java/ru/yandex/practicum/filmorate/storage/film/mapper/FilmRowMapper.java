package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film f = new Film();

        f.setId(rs.getLong("id"));
        f.setName(rs.getString("name"));
        f.setDescription(rs.getString("description"));
        f.setReleaseDate(
                LocalDate.parse(rs.getString("release_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        f.setDuration(rs.getInt("duration"));

        Set<Long> likes = new HashSet<>();
        likes.add(rs.getLong("likes"));
        f.setLikes(likes);

        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("mpa"));
        mpa.setName(rs.getString("mpa_name"));
        f.setMpa(mpa);

        String genreIds = rs.getString("genre_ids");
        String genreNames = rs.getString("genre_names");
        if (genreIds != null && genreNames != null) {
            String[] arrIds = genreIds.split(",");
            String[] arrNames = genreNames.split(",");
            if (arrIds.length == arrNames.length) {
                Set<Genre> genres = new HashSet<>();
                for (int i = 0; i < arrIds.length; i++) {
                    Genre genre = new Genre();
                    Long genreId = Long.valueOf(arrIds[i].trim());
                    String genreName = arrNames[i].trim();
                    genre.setId(genreId);
                    genre.setName(genreName);
                    genres.add(genre);
                }
                f.setGenres(genres);
            }
        }

        return f;
    }
}
