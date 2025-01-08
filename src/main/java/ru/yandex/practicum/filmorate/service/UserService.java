package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validation(user);
        return userStorage.addUser(user);
    }

    public User update(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUserById(Long id) {
        userStorage.deleteUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        if (friend == null) {
            throw new NotFoundException("Friend not " + friendId + " found");
        }

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        log.info("User found: {}", user);
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    private void validation(User user) {
        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("Email should contain symbol @");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login should not contain spaces");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
