package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InMemoryFilmStorageTest {

    @InjectMocks
    private InMemoryFilmStorage filmStorage;

    private Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        film1 = new Film("nnn", "jfnid", LocalDate.of(2020, 1, 1), 120);
        film2 = new Film("mmm", "jfnid", LocalDate.of(2020, 1, 1), 120);
    }

    @Test
    public void checkMethodGetFilm() {
        filmStorage.addFilm(film1);
        Assertions.assertEquals(film1, filmStorage.getFilm(1));
    }

    @Test
    public void checkMethodGetFilms() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        Assertions.assertEquals(List.of(film1, film2), new ArrayList<>(filmStorage.getFilms()));
    }

    @Test
    public void checkMethodAddFilm() {
        filmStorage.addFilm(film1);
        Assertions.assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    public void checkMethodUpdateFilm() {
        filmStorage.addFilm(film1);
        film2.setId(film1.getId());
        filmStorage.updateFilm(film2);
        Assertions.assertEquals(film2, filmStorage.getFilm(film1.getId()));
    }

    @Test
    public void checkMethodAddLike() {
        User user = new User("ddd@mail.ru", "dddd", "dfd dfd", LocalDate.of(2000, 1, 1));
        user.setId(1);
        filmStorage.addFilm(film1);
        Film result = filmStorage.addLike(film1.getId(), user.getId());
        film1.addLike(user.getId());
        Assertions.assertEquals(film1, result);
    }

    @Test
    public void checkMethodDeleteLike() {
        User user = new User("ddd@mail.ru", "dddd", "dfd dfd", LocalDate.of(2000, 1, 1));
        user.setId(1);
        film1.addLike(user.getId());
        filmStorage.addFilm(film1);
        Film result = filmStorage.deleteLike(film1.getId(), user.getId());
        film1.deleteLike(user.getId());
        Assertions.assertEquals(film1, result);
    }

    @Test
    public void checkMethodGetPopularFilms() {
        User user = new User("ddd@mail.ru", "dddd", "dfd dfd", LocalDate.of(2000, 1, 1));
        user.setId(1);
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        filmStorage.addLike(film2.getId(), user.getId());
        Assertions.assertEquals(List.of(film2), new ArrayList<>(filmStorage.getPopularFilms(1)));
    }
}
