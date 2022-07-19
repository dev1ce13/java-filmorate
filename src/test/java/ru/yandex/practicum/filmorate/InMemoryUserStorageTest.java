package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InMemoryUserStorageTest {

    @InjectMocks
    private InMemoryUserStorage userStorage;

    private User user1;
    private User user2;

    @BeforeEach
    public void beforeEach() {
        user1 = new User("yyy@gmail.com", "rrrr", "rr rr", LocalDate.of(2010, 1, 1));
        user2 = new User("ddd@mail.ru", "dddd", "dfd dfd", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void checkMethodGetUsers() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        Assertions.assertEquals(List.of(user1, user2), new ArrayList<>(userStorage.getUsers()));
    }

    @Test
    public void checkMethodGetUser() {
        userStorage.createUser(user1);
        Assertions.assertEquals(user1, userStorage.getUser(1));
    }

    @Test
    public void checkMethodCreateUser() {
        userStorage.createUser(user1);
        Assertions.assertEquals(1, userStorage.getUsers().size());
    }

    @Test
    public void checkMethodUpdateUser() {
        userStorage.createUser(user1);
        user2.setId(1);
        userStorage.updateUser(user2);
        Assertions.assertEquals(user2, userStorage.getUser(user1.getId()));
    }

    @Test
    public void checkMethodAddFriend() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.addFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(1, user1.getFriendsId().size());
    }

    @Test
    public void checkMethodDeleteFriend() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        user1.addFriend(user2.getId());
        userStorage.deleteFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(0, user1.getFriendsId().size());
    }

    @Test
    public void checkMethodGetFriends() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.addFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(List.of(user2), userStorage.getFriends(user1.getId()));
    }

    @Test
    public void checkMethodGetCommonFriends() {
        User user3 = new User("qqqq@gmail.com", "qwerty", "qwertyqaz", LocalDate.of(1990, 2, 2));
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        userStorage.addFriend(user1.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user3.getId());
        Assertions.assertEquals(List.of(user3), new ArrayList<>(userStorage.getCommonFriends(user1.getId(), user2.getId())));
    }
}
