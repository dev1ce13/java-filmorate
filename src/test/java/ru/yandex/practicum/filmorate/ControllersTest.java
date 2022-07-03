package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ControllersTest {
    private UserController userController;
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        filmController = new FilmController();
    }

    @Test
    public void createUserWithBirthdayIsFuture() {
        User user = new User("ggg@mail.ru", "asdf", "name", LocalDate.now().plusDays(1));
        ResponseEntity<Object> response = userController.createUser(user);
        Object responseBody = response.getBody();
        HttpStatus responseCode = response.getStatusCode();
        Assertions.assertEquals("Некорректные данные", responseBody);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseCode);
    }

    @Test
    public void createUserWithSpaceInLogin() {
        User user = new User("ggg@mail.ru", "a b", "name", LocalDate.of(2020, 1, 1));
        ResponseEntity<Object> response = userController.createUser(user);
        Object responseBody = response.getBody();
        HttpStatus responseCode = response.getStatusCode();
        Assertions.assertEquals("Некорректные данные", responseBody);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseCode);
    }

    @Test
    public void createUserWithBlankName() {
        User user = new User("ggg@mail.ru", "ab", "", LocalDate.of(2020, 1, 1));
        ResponseEntity<Object> response = userController.createUser(user);
        Object responseBody = response.getBody();
        HttpStatus responseCode = response.getStatusCode();
        assert responseBody != null;
        Assertions.assertTrue(responseBody.toString().contains("name=ab"));
        Assertions.assertEquals(HttpStatus.OK, responseCode);
    }

    @Test
    public void addFilmWithDescriptionLength201() {
        Film film = new Film("a",
                "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwww rrrrrrrrrrtttttttttt",
                LocalDate.of(2000, 1, 1),
                10);
        ResponseEntity<Object> response = filmController.addFilm(film);
        Object responseBody = response.getBody();
        HttpStatus responseCode = response.getStatusCode();
        Assertions.assertEquals("Некорректные данные", responseBody);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseCode);
    }

    @Test
    public void addFilmWithReleaseDateBefore1895_12_28() {
        Film film = new Film("a", "q", LocalDate.of(1895, 12, 27), 10);
        ResponseEntity<Object> response = filmController.addFilm(film);
        Object responseBody = response.getBody();
        HttpStatus responseCode = response.getStatusCode();
        Assertions.assertEquals("Некорректные данные", responseBody);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseCode);
    }

    @Test
    public void addFilmWithDurationIsNonPositive() {
        Film film = new Film("a", "q", LocalDate.of(1900, 12, 27), 0);
        ResponseEntity<Object> response = filmController.addFilm(film);
        Object responseBody = response.getBody();
        HttpStatus responseCode = response.getStatusCode();
        Assertions.assertEquals("Некорректные данные", responseBody);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseCode);
    }
}
