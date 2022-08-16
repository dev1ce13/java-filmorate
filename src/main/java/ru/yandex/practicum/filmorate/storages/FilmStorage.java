package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getFilms();

    Film getFilm(int filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    Collection<Film> getPopularFilms(int count);

    Collection<Film> getPopularFilmsByGenre(int count, int genreId);

    Collection<Film> getPopularFilmsByYear(int count, int year);

    Collection<Film> getPopularFilms(int count, int genreId, int year);

    Collection<Film> getCommonFilms(int userId, int friendId);
}
