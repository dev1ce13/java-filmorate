package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int userId) {
        try {
            return userStorage.getUser(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с ID " + userId + " не существует");
        }
    }

    public User createUser(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        try {
            userStorage.getUser(user.getId());
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return userStorage.updateUser(user);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с ID " + user.getId() + " не существует");
        }
    }

    public User addFriend(int userId, int friendId) {
        try {
            userStorage.getUser(userId);
            userStorage.getUser(friendId);
            if (userId == friendId) {
                log.error("Нельзя добавить в друзья самого себя");
                throw new ValidationException("Нельзя добавить в друзья самого себя");
            }
            return userStorage.addFriend(userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с ID: " + userId + " или пользователя с ID: " + friendId + " не существует");
        }
    }

    public User deleteFriend(int userId, int friendId) {
        try {
            userStorage.getUser(userId);
            userStorage.getUser(friendId);
            if (userId == friendId) {
                log.error("Нельзя добавить в друзья самого себя");
                throw new ValidationException("Нельзя добавить в друзья самого себя");
            }
            return userStorage.deleteFriend(userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с ID: " + userId + " или пользователя с ID: " + friendId + " не существует");
        }
    }

    public Collection<User> getFriends(int userId) {
        try {
            userStorage.getUser(userId);
            return userStorage.getFriends(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с ID " + userId + " не существует");
        }
    }

    public Collection<User> getCommonFriends(int userId, int otherId) {
        try {
            if (userId == otherId) {
                log.error("Нельзя посмотреть список общих друзей с самим собой");
                throw new ValidationException("Нельзя посмотреть список общих друзей с самим собой");
            }
            return userStorage.getCommonFriends(userId, otherId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с ID: " + userId + " или пользователя с ID: " + otherId + " не существует");
        }
    }

    public Collection<Film> getRecommendation(int userId) {
        try {
            userStorage.getUser(userId);
            return filmStorage.getRecommendation(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с ID " + userId + " не существует");
        }
    }
}
