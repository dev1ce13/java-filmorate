package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private String name;
    private final LocalDate birthday;
    private Set<Integer> friendsId = new HashSet<>();
    private HashMap<Integer, Boolean> friends = new HashMap<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(int id) {
        friendsId.add(id);
        //friends.put(id, false);
    }

    public void deleteFriend(int id) {
        friendsId.remove(id);
        //friends.remove(id);
    }

    public void setStatusFriendship(int id) {
        friends.replace(id, true);
    }
}
