package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
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
        try {
            filmStorage.getFilm(filmId);
            return filmStorage.getFilm(filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильма не существует");
        }
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        try {
            filmStorage.getFilm(film.getId());
            return filmStorage.updateFilm(film);
        } catch (EmptyResultDataAccessException e) {
            log.error("Данных не существует");
            throw new NotFoundException("Фильма не существует");
        }
    }

    public Film addLike(int filmId, int userId) {
        try {
            filmStorage.getFilm(filmId);
            userStorage.getUser(userId);
            return filmStorage.addLike(filmId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильма не существует");
        }
    }

    public Film deleteLike(int filmId, int userId) {
        try {
            filmStorage.getFilm(filmId);
            userStorage.getUser(userId);
            return filmStorage.deleteLike(filmId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильма не существует");
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
