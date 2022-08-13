package ru.yandex.practicum.filmorate.storages.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storages.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::makeGenre, genreId);
    }

    @Override
    public Collection<Genre> getGenresByFilmId(int filmId) {
        String sql = "SELECT G.* FROM GENRES G " +
                "JOIN FILM_GENRE FG on G.GENRE_ID = FG.GENRE_ID " +
                "WHERE FG.FILM_ID = ? ";
        return jdbcTemplate.query(sql, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
