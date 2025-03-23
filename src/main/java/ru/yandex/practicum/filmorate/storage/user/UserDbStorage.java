package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Primary
@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    private static final String CREATE_USER_QUERY = "insert into users(name, email, login, birthday) values (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "update users set name = ?, email = ?, login = ?, birthday = ? where id = ?";
    private static final String FIND_BY_ID_QUERY = "select *, (select count(friend_id) from friends where user_id = id) as friends from users where id = ?";
    private static final String FIND_ALL_QUERY = "select *, (select count(friend_id) from friends where user_id = id) as friends from users";
    private static final String GET_FRIENDS_QUERY = "select friend_id from friends where user_id = ?";
    private static final String ADD_FRIEND_QUERY = "insert into friends(user_id, friend_id) values(?, ?)";
    private static final String GET_USER_FRIENDS_QUERY = "select t2.*, (select count(friend_id) from friends where user_id = t2.id) as friends " +
            "from friends t1 " +
            "inner join users t2 on t2.id = t1.friend_id " +
            "where t1.user_id = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "select *, (select count(friend_id) from friends where user_id = id) as friends " +
            "from users " +
            "where id in (select friend_id from friends where user_id = ?) " +
            "and id in (select friend_id from friends where user_id = ?)";
    private static final String DELETE_FRIEND_QUERY = "delete from friends where user_id = ? and friend_id = ?";
    private static final String ACTUALIZE_FRIENDS_FALSE_QUERY = "update friends set approved = false " +
            "where user_id = ? and friend_id = ?";
    private static final String ACTUALIZE_FRIENDS_TRUE_QUERY = "update friends set approved = true " +
            "where ((user_id = ? and friend_id = ?) or (user_id = ? and friend_id = ?)) " +
            "and exists(select user_id, friend_id from friends where user_id = ? and friend_id = ?) " +
            "and exists(select user_id, friend_id from friends where user_id = ? and friend_id = ?)";


    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public User findById(Long userId) {
        return jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, userId);
    }

    @Override
    public Collection<User> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getName());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            user.setId(id);
            return user;
        } else {
            throw new InternalServerException("Couldn't save data");
        }
    }

    @Override
    public User update(User newUser) {
        int rowsUpdated = jdbc.update(UPDATE_USER_QUERY, newUser.getName(), newUser.getLogin(), newUser.getEmail(), newUser.getBirthday(), newUser.getId());

        if (rowsUpdated > 0)
            return newUser;
        else
            throw new NotFoundException("User with Id " + newUser.getId() + " does not exist");
    }

    @Override
    public Collection<User> findFriends(Long userId) {
//        return jdbc.query(FIND_ALL_FRIENDS, new Object[]{id}, extractor);
        List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "users", List.of(userId));
        if (checkVals.isEmpty()) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return jdbc.query(GET_USER_FRIENDS_QUERY, mapper, userId);
    }

    @Override
    public Collection<User> findCommonFriends(Long userId, Long friendId) {
        return jdbc.query(FIND_COMMON_FRIENDS_QUERY, mapper, userId, friendId);
    }

    @Override
    public Set<Long> addFriend(Long userId, Long friendId) {
        List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "users", List.of(userId, friendId));
        if (checkVals.size() != 2) {
            throw new NotFoundException("Couldn't find users by IDs");
        }
        Set<Long> friends = getFriends(userId);
        if (!friends.contains(friendId)) {
            int rowsUpdated = jdbc.update(ADD_FRIEND_QUERY, userId, friendId);
            if (rowsUpdated > 0) {
                friends.add(friendId);
                jdbc.update(ACTUALIZE_FRIENDS_TRUE_QUERY, userId, friendId, friendId, userId, userId, friendId, friendId, userId);
            }
        }
        return friends;
    }

    @Override
    public Set<Long> deleteFriend(Long userId, Long friendId) {
//        return jdbc.update(DELETE_FRIEND, userId, friendId) > 0;
        List<Long> checkVals = DatabaseUtils.getExistRows(jdbc, "users", List.of(userId, friendId));
        if (checkVals.size() != 2) {
            throw new NotFoundException("User with ids not found");
        }
        int rowsUpdated = jdbc.update(DELETE_FRIEND_QUERY, userId, friendId);
        if (rowsUpdated > 0) {
            jdbc.update(ACTUALIZE_FRIENDS_FALSE_QUERY, friendId, userId);
        }
        return getFriends(userId);
    }

    public Set<Long> getFriends(Long userId) {
        List<Long> userIds = jdbc.queryForList(GET_FRIENDS_QUERY, Long.class, userId);
        return new HashSet<>(userIds);
    }
}
