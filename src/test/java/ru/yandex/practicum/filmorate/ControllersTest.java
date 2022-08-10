package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ControllersTest {
    private final UserController userController;
    private final FilmController filmController;

    @Test
    public void createUserWithBirthdayIsFuture() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = User.builder()
                                .id(1)
                                .email("ggg@mail.ru")
                                .login("asdf")
                                .name("name")
                                .birthday(LocalDate.now().plusDays(1))
                                .build();
                        userController.createUser(user);
                    }
                }
        );
    }

    @Test
    public void createUserWithSpaceInLogin() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = User.builder()
                                .id(1)
                                .email("ggg@mail.ru")
                                .login("a b")
                                .name("name")
                                .birthday(LocalDate.of(2020, 1, 1))
                                .build();
                        userController.createUser(user);
                    }
                }
        );
    }

    @Test
    public void createUserWithBlankName() {
        User user = User.builder()
                .id(1)
                .email("ggg@mail.ru")
                .login("ab")
                .name("")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        User response = userController.createUser(user);
        Assertions.assertEquals(user.getLogin(), response.getName());
    }

    @Test
    public void addFilmWithDescriptionLength201() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = Film.builder()
                                .name("a")
                                .description("qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwwwrrrrrrrrrrtttttttttt" +
                                        "qqqqqqqqqqwwwwwwwwwwwwwwwwwwww rrrrrrrrrrtttttttttt")
                                .releaseDate(LocalDate.of(2000, 1, 1))
                                .duration(10)
                                .mpa(Mpa.builder().id(1).name("a").build())
                                .build();
                        filmController.addFilm(film);
                    }
                }
        );
    }

    @Test
    public void addFilmWithReleaseDateBefore1895_12_28() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = Film.builder()
                                .name("a")
                                .description("q")
                                .releaseDate(LocalDate.of(1895, 12, 27))
                                .duration(10)
                                .mpa(Mpa.builder().id(1).name("a").build())
                                .build();
                        filmController.addFilm(film);
                    }
                }
        );
    }

    @Test
    public void addFilmWithDurationIsNonPositive() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = Film.builder()
                                .name("a")
                                .description("q")
                                .releaseDate(LocalDate.of(1900, 12, 27))
                                .duration(0)
                                .mpa(Mpa.builder().id(1).name("a").build())
                                .build();
                        filmController.addFilm(film);
                    }
                }
        );
    }
}
