package ru.yandex.practicum.filmorate.service.users;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

public interface UserService {
    User addFriend(Long id, Long friendId) throws NotFoundException;

    User deleteFriend(Long id, Long friendId) throws NotFoundException;

    List<User> findAllFriends(Long id) throws NotFoundException;

    List<User> findAllCommonFriends(Long id, Long otherId) throws NotFoundException;

    Collection<User> findAll();

    User create(User user) throws DuplicatedDataException;

    User update(User newUser) throws DuplicatedDataException, NotFoundException;
}
