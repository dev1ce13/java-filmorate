package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import java.time.LocalDate;

public class ControllersTest {
    private UserController userController;
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @Test
    public void createUserWithBirthdayIsFuture() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User("ggg@mail.ru", "asdf", "name", LocalDate.now().plusDays(1));
                        User response = userController.createUser(user);
                    }
                }
        );
    }

    @Test
    public void createUserWithSpaceInLogin() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User("ggg@mail.ru", "a b", "name", LocalDate.of(2020, 1, 1));
                        User response = userController.createUser(user);
                    }
                }
        );
    }

    @Test
    public void createUserWithBlankName() {
        User user = new User("ggg@mail.ru", "ab", "", LocalDate.of(2020, 1, 1));
        User response = userController.createUser(user);
        Assertions.assertEquals(user.getLogin(), response.getName());
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
                        Film response = filmController.addFilm(film);
                    }
                }
        );
    }

    @Test
    public void addFilmWithReleaseDateBefore1895_12_28() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film("a", "q", LocalDate.of(1895, 12, 27), 10);
                        Film response = filmController.addFilm(film);
                    }
                }
        );
    }

    @Test
    public void addFilmWithDurationIsNonPositive() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film("a", "q", LocalDate.of(1900, 12, 27), 0);
                        Film response = filmController.addFilm(film);
                    }
                }
        );
    }
}

