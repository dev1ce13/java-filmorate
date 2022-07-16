package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;


public class ServicesTest {
    private UserService userService;
    private FilmService filmService;

    @BeforeEach
    public void beforeEach() {
        userService = new UserService(new InMemoryUserStorage());
        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
    }

    @Test
    public void getNotExistUser() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User response = userService.getUser(1);
                    }
                }
        );
    }

    @Test
    public void getNotExistFilm() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film response = filmService.getFilm(1);
                    }
                }
        );
    }
}
