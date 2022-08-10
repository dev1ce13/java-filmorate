package ru.yandex.practicum.filmorate.storages.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;


@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public User getUser(int userId) {
        String sql = "SELECT * FROM USERS WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeUser, userId);
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS(email, login, name, birthday)" +
                " VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET email = ?, " +
                "login = ?, " +
                "name = ?, " +
                "birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User addFriend(int userId, int friendId) {
        String sql = "INSERT INTO FRIENDS(user_id, friend_id) VALUES (?, ?)";
        //jdbcTemplate.update(sql, friendId, userId);
        jdbcTemplate.update(sql, userId, friendId);
        return getUser(userId);
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDS WHERE user_id = ? AND friend_id = ?";
        //jdbcTemplate.update(sql, friendId, userId);
        jdbcTemplate.update(sql, userId, friendId);
        return getUser(userId);
    }

    @Override
    public Collection<User> getFriends(int userId) {
        String sql = "SELECT * " +
                "FROM USERS U " +
                "WHERE U.user_id IN(" +
                "SELECT friend_id " +
                "FROM FRIENDS F " +
                "WHERE F.user_id = ?)";
        return jdbcTemplate.query(sql, this::makeUser, userId);
    }

    @Override
    public Collection<User> getCommonFriends(int userId, int otherId) {
        String sql = "SELECT users.* " +
                "FROM USERS " +
                "INNER JOIN friends ON users.user_id = friends.FRIEND_ID " +
                "WHERE friends.user_id = ? " +
                "AND friend_id IN (" +
                "    SELECT friend_id " +
                "    FROM friends " +
                "    WHERE friends.user_id = ?)";
        return jdbcTemplate.query(sql, this::makeUser, userId, otherId);
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
