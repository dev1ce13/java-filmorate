package ru.yandex.practicum.filmorate.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("/GET вывод всех фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(
            @PathVariable int filmId
    ) {
        log.info("/GET вывод фильма с ID: " + filmId);
        return filmService.getFilm(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10") int count
    ) {
        log.info("/GET получение списка популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "friendId") int friendId
    ) {
        log.info("/GET вывод списка общих фильмов");
        return filmService.getCommonFilms(userId, friendId);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("/POST добавление нового фильма");
        if (notValidate(film)) {
            log.error("данные не прошли валидацию");
            throw new ValidationException("Некорректные данные");
        }
        log.info("Фильм успешно добавлен");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("/PUT обновление фильма c ID: " + film.getId());
        if (notValidate(film)) {
            log.error("данные не прошли валидацию");
            throw new ValidationException("Некорректные данные");
        }
        log.info("Фильм успешно обновлен");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(
            @PathVariable int filmId,
            @PathVariable int userId
    ) {
        log.info("/PUT добавление лайка");
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(
            @PathVariable int filmId,
            @PathVariable int userId
    ) {
        log.info("/DELETE удаление лайка");
        return filmService.deleteLike(filmId, userId);
    }

    public boolean notValidate(Film film) {
        return film.getDescription().length() > 200 ||
                !film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)) ||
                film.getDuration() <= 0;
    }
}
