package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

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
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User("ggg@mail.ru", "asdf", "name", LocalDate.now().plusDays(1));
                        ResponseEntity<User> response = userController.createUser(user);
                    }
                }
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void createUserWithSpaceInLogin() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User("ggg@mail.ru", "a b", "name", LocalDate.of(2020, 1, 1));
                        ResponseEntity<User> response = userController.createUser(user);
                    }
                }
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void createUserWithBlankName() {
        User user = new User("ggg@mail.ru", "ab", "", LocalDate.of(2020, 1, 1));
        ResponseEntity<User> response = userController.createUser(user);
        Assertions.assertEquals(user.getLogin(), Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    public void addFilmWithDescriptionLength201() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film("a",
                                "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwww rrrrrrrrrrtttttttttt",
                                LocalDate.of(2000, 1, 1),
                                10);
                        ResponseEntity<Film> response = filmController.addFilm(film);
                    }
                }
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void addFilmWithReleaseDateBefore1895_12_28() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film("a", "q", LocalDate.of(1895, 12, 27), 10);
                        ResponseEntity<Film> response = filmController.addFilm(film);
                    }
                }
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void addFilmWithDurationIsNonPositive() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film("a", "q", LocalDate.of(1900, 12, 27), 0);
                        ResponseEntity<Film> response = filmController.addFilm(film);
                    }
                }
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
}
