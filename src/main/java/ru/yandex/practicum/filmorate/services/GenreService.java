package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storages.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Collection<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int genreId) {
        try {
            return genreStorage.getGenreById(genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанра с ID " + genreId + " не существует");
        }
    }

    public Collection<Genre> getGenresByFilmId(int filmId) {
        return genreStorage.getGenresByFilmId(filmId);
    }
}
