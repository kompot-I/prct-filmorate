package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.Set;

public interface UserStorage extends Storage<User> {

    Collection<User> findFriends(Long id);

    Collection<User> findCommonFriends(Long id, Long otherId);

    Set<Long> addFriend(Long userId, Long friendId);

    Set<Long> deleteFriend(Long userId, Long friendId);
}
