package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(int filmId) {
        if (filmStorage.getFilm(filmId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Фильма не существует");
        }
        return filmStorage.getFilm(filmId);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilm(film.getId()) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Фильма не существует");
        }
        return filmStorage.updateFilm(film);
    }

    public Film addLike(int filmId, int userId) {
        if (filmStorage.getFilm(filmId) == null || userStorage.getUser(userId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Фильма с ID: " + filmId + " или пользователя с ID: " + userId + " не существует");
        }
        return filmStorage.addLike(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) {
        if (filmStorage.getFilm(filmId) == null || userStorage.getUser(userId) == null) {
            log.error("Данных не существует");
            throw new NotFoundException("Фильма с ID: " + filmId + " или пользователя с ID: " + userId + " не существует");
        }
        return filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
