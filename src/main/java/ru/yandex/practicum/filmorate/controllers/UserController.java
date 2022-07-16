package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("/GET вывод всех пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(
            @PathVariable int userId
    ) {
        log.info("/GET вывод пользователя с ID: " + userId);
        return userService.getUser(userId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriends(
            @PathVariable int userId
    ) {
        log.info("/GET вывод всех друзей пользователя");
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable int userId,
            @PathVariable int otherId
    ) {
        log.info("/GET вывод общих друзей пользователей c ID: " + userId + " и " + otherId);
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("/POST создание нового пользователя");
        if (notValidate(user)) {
            log.error("Данные не прошли валидацию");
            throw new ValidationException("Некорректные данные");
        }
        log.info("Пользователь успешно создан");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("/PUT обновление пользователя c ID: " + user.getId());
        if (notValidate(user)) {
            log.error("данные не прошли валидацию");
            throw new ValidationException("Некорректные данные");
        }
        log.info("Пользователь успешно обновлен");
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(
            @PathVariable int userId,
            @PathVariable int friendId
    ) {
        log.info("/PUT добавление в друзья пользователей с ID: " + userId + " и " + friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(
            @PathVariable int userId,
            @PathVariable int friendId
    ) {
        log.info("/DELETE удаление из друзей пользователей с ID: " + userId + " и " + friendId);
        return userService.deleteFriend(userId, friendId);
    }

    public boolean notValidate(User user) {
        return user.getLogin().contains(" ") ||
                !user.getBirthday().isBefore(LocalDate.now().plusDays(1));
    }
}
