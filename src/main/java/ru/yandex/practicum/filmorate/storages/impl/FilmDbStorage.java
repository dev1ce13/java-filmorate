package ru.yandex.practicum.filmorate.storages.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;
import ru.yandex.practicum.filmorate.services.MpaService;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;

@Component("FilmDbStorage")
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    @Override
    public Collection<Film> getFilms() {
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public Film getFilm(int filmId) {
        String sql = "SELECT F.* FROM FILMS F " +
                "WHERE F.film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeFilm, filmId);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlFilm = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)" +
                " VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilm, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        String sqlGenre = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) " +
                "VALUES(?, ?)";
        if (film.getGenres() != null) {
            LinkedHashSet<Genre> genres = new LinkedHashSet<>();
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlGenre,
                        film.getId(),
                        genre.getId());
                genres.add(genreService.getGenreById(genre.getId()));
            }
            film.setGenres(genres);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILMS SET NAME = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ?, " +
                "RATING_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        String sqlDeleteGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        String sqlSetGenre = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) " +
                "VALUES(?, ?)";
        if (film.getGenres() != null) {
            jdbcTemplate.update(sqlDeleteGenres, film.getId());
            LinkedHashSet<Genre> genres = new LinkedHashSet<Genre>();
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlSetGenre,
                        film.getId(),
                        genre.getId());
                genres.add(genreService.getGenreById(genre.getId()));
            }
            film.setGenres(genres);
        }
        return film;
    }

    @Override
    public Film addLike(int filmId, int userId) {
        String sql = "INSERT INTO FILM_LIKE(film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        return getFilm(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM FILM_LIKE WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        return getFilm(filmId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        String sql = "SELECT films.* " +
                "FROM films " +
                "LEFT JOIN film_like ON films.film_id = film_like.film_id " +
                "GROUP BY films.film_id " +
                "ORDER BY COUNT(film_like.film_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, this::makeFilm, count);
    }

    @Override
    public Collection<Film> getCommonFilms(int userId, int friendId) {
        String sql = "SELECT F.* " +
                "FROM FILMS F " +
                "INNER JOIN FILM_LIKE FL on F.FILM_ID = FL.FILM_ID " +
                "INNER JOIN ( " +
                    "SELECT FILM_ID, " +
                    "COUNT(FILM_ID) COUNT_LIKE " +
                    "FROM FILM_LIKE " +
                    "GROUP BY FILM_ID " +
                ") AS LIKES ON F.FILM_ID = LIKES.FILM_ID " +
                "WHERE USER_ID = ? " +
                "AND FL.FILM_ID IN ( " +
                    "SELECT FILM_ID " +
                    "FROM FILM_LIKE " +
                    "WHERE USER_ID = ? " +
                ") " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY LIKES.COUNT_LIKE DESC";
        return jdbcTemplate.query(sql, this::makeFilm, userId, friendId);
    }

    private Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaService.getMpaById(resultSet.getInt("rating_id")))
                .genres(new LinkedHashSet<>(genreService.getGenresByFilmId(resultSet.getInt("film_id"))))
                .build();
    }
}
