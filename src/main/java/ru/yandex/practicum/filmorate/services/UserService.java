package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;

    public Collection<User> getUsers() {
        return storage.getUsers();
    }

    public User getUser(int userId) {
        if (storage.getUser(userId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Пользователя с ID " + userId + " не существует");
        }
        return storage.getUser(userId);
    }

    public User createUser(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        if (storage.getUser(user.getId()) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Пользователя с ID " + user.getId() + " не существует");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.updateUser(user);
    }

    public User addFriend(int userId, int friendId) {
        if (storage.getUser(userId) == null || storage.getUser(friendId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Пользователя с ID: " + userId + " или пользователя с ID: " + friendId + " не существует");
        }
        return storage.addFriend(userId, friendId);
    }

    public User deleteFriend(int userId, int friendId) {
        if (storage.getUser(userId) == null || storage.getUser(friendId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Пользователя с ID: " + userId + " или пользователя с ID: " + friendId + " не существует");
        }
        return storage.deleteFriend(userId, friendId);
    }

    public Collection<User> getFriends(int userId) {
        if (storage.getUser(userId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Пользователя с ID " + userId + " не существует");
        }
        return storage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(int userId, int otherId) {
        if (storage.getUser(userId) == null || storage.getUser(otherId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Пользователя с ID: " + userId + " или пользователя с ID: " + otherId + " не существует");
        }
        return storage.getCommonFriends(userId, otherId);
    }
}
