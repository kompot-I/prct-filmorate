package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private static long tempId = 0;
    private final HashMap<Long, User> userHashMap = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userHashMap.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        usernameCheck(user);
        user.setId(++tempId);
        userHashMap.put(user.getId(), user);
        log.info("Пользователь " + user.getName() + " добавлен");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!userHashMap.containsKey(user.getId())) {
            log.debug("Пользователь не найден");
            throw new ValidationException("Пользователь не найден");
        }
        usernameCheck(user);
        userHashMap.put(user.getId(), user);
        log.info("Данные пользователя " + user.getLogin() + " обновлены");
        return user;
    }

    private void usernameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}