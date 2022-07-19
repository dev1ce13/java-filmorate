package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @InjectMocks
    private FilmService filmService;

    @Mock
    private FilmStorage filmStorage;
    @Mock
    private UserStorage userStorage;

    @Test
    public void getNotExistFilm() {
        Assertions.assertThrows(NotFoundException.class, () -> filmService.getFilm(1));
    }

    @Test
    public void addLikeWithNotExistUser() {
        Film film = new Film("fff", "fff", LocalDate.of(2020, 1, 1), 100);
        filmService.addFilm(film);
        Assertions.assertThrows(NotFoundException.class, () -> filmService.addLike(1, 1));
    }
}
