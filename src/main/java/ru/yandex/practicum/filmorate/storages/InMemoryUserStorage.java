package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        user.setId(generateID());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public User addFriend(int userId, int friendId) {
        users.get(userId).addFriend(friendId);
        users.get(friendId).addFriend(userId);
        return users.get(userId);
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        users.get(userId).deleteFriend(friendId);
        users.get(friendId).deleteFriend(userId);
        return users.get(userId);
    }

    @Override
    public Collection<User> getFriends(int userId) {
        Collection<User> friends = new ArrayList<>();
        for (Integer id : users.get(userId).getFriendsId()) {
            friends.add(users.get(id));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(int userId, int otherId) {
        Collection<User> commonFriends = new ArrayList<>();
        for (Integer id : users.get(userId).getFriendsId()) {
            if (users.get(otherId).getFriendsId().contains(id)) {
                commonFriends.add(users.get(id));
            }
        }
        return commonFriends;
    }

    private int generateID() {
        return ++id;
    }
}
