package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {

    Collection<Genre> getGenres();

    Genre getGenreById(int genreId);

    Collection<Genre> getGenresByFilmId(int filmId);
}
