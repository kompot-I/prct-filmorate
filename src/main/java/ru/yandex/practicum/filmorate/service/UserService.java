package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validation(user);
        log.info("User " + user.getName() + " has been added");
        return userStorage.addUser(user);
    }

    public User update(User user) {
        validation(user);
        return userStorage.updateUser(user);
    }

    public void deleteUserById(Long id) {
        userStorage.deleteUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend with id " + friendId + " not found"));

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));

        log.info("User found: {}", user);
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend with id " + friendId + " not found"));

        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        User otherUser = userStorage.getUserById(otherId)
                .orElseThrow(() -> new NotFoundException("Common friend with id " + otherId + " not found"));

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(friendId -> userStorage.getUserById(friendId)
                        .orElseThrow(() -> new NotFoundException("Friend with id " + friendId + " not found")))
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
