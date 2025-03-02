package ru.yandex.practicum.filmorate.storage.rating.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RatingResultSetExtractor implements ResultSetExtractor<List<Rating>> {
    @Override
    public List<Rating> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Rating> ratings = new ArrayList<>();
        while (rs.next()) {
            Rating r = new Rating();
            r.setId(rs.getLong("id"));
            r.setName(rs.getString("name"));

            ratings.add(r);
        }

        return ratings;
    }
}
