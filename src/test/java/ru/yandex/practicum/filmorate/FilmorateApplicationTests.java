package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storages.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storages.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storages.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Test
    public void testGetUserById() {
        User user1 = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2001, 1, 1))
                .build();
        userStorage.createUser(user1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetFilmById() {
        Film film1 = Film.builder()
                .name("film")
                .description("desc")
                .releaseDate(LocalDate.of(2020, 2, 2))
                .duration(100)
                .mpa(mpaStorage.getMpaById(1))
                .build();
        filmStorage.addFilm(film1);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetMpaById() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpaById(1));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetGenreById() {
        Optional<Genre> userOptional = Optional.ofNullable(genreStorage.getGenreById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}
