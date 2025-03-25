package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("ID is invalid");
        }
        return userStorage.update(user);
    }

    public Set<Long> addFriend(Long userId, Long friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public Set<Long> deleteFriend(Long userId, Long friendId) {
        return userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> findAllFriends(Long userId) {
        return userStorage.findFriends(userId);
    }

    public Collection<User> findCommonFriends(Long userId, Long friendId) {
        return userStorage.findCommonFriends(userId, friendId);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long userId) {
        return userStorage.findById(userId);
    }

}