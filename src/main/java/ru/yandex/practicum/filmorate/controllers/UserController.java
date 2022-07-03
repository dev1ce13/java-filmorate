package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        log.info("/GET вывод всех пользователей");

        return new ResponseEntity<>(users.values(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        log.info("/POST создание нового пользователя");

        if (validate(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(generateID());
            users.put(user.getId(), user);

            log.info("Пользователь успешно создан");

            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.error("данные не прошли валидацию");

            return new ResponseEntity<>("Некорректные данные", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(@Valid @RequestBody User user) {
        log.info("/PUT обновление пользователя c ID: " + user.getId());

        if (validate(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            User response = users.replace(user.getId(), user);
            if (response != null) {
                log.info("Пользователь успешно обновлен");

                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                log.info("Пользователя с ID " + user.getId() + " не существует");

                return new ResponseEntity<>("Пользователя с ID " + user.getId() + " не существует", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            log.error("данные не прошли валидацию");

            return new ResponseEntity<>("Некорректные данные", HttpStatus.BAD_REQUEST);
        }
    }

    private int generateID() {
        id ++;
        return id;
    }

    public boolean validate(User user) {
        return !user.getLogin().contains(" ") &&
                user.getBirthday().isBefore(LocalDate.now().plusDays(1));
    }
}
