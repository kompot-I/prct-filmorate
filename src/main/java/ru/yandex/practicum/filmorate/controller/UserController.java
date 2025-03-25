package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable("userId") Long userId) {
        return userService.findById(userId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> findAllFriends(@PathVariable("userId") Long userId) {
        return userService.findAllFriends(userId);
    }

    @PutMapping("/{userId}/friends/{friendsId}")
    public Set<Long> addFriend(@PathVariable("userId") Long userId, @PathVariable Long friendsId) {
        return userService.addFriend(userId, friendsId);
    }

    @DeleteMapping("/{userId}/friends/{friendsId}")
    public Set<Long> deleteFriend(@PathVariable("userId") Long userId, @PathVariable Long friendsId) {
        return userService.deleteFriend(userId, friendsId);
    }


    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> findAllCommonFriends(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        return userService.findCommonFriends(userId, friendId);
    }

}
