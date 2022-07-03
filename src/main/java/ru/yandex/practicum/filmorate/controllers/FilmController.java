package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        log.info("/GET вывод всех фильмов");

        return new ResponseEntity<>(films.values(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addFilm(@Valid @RequestBody Film film) {
        log.info("/POST добавление нового фильма");

        if (validate(film)) {
            film.setId(generateID());
            films.put(film.getId(), film);

            log.info("Фильм успешно добавлен");

            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            log.error("данные не прошли валидацию");

            return new ResponseEntity<>("Некорректные данные", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateFilm(@Valid @RequestBody Film film) {
        log.info("/PUT обновление фильма c ID: " + film.getId());

        if (validate(film)) {
            Film response = films.replace(film.getId(), film);
            if (response != null) {
                log.info("Фильм успешно обновлен");

                return new ResponseEntity<>(film, HttpStatus.OK);
            } else {
                log.info("Фильма с ID " + film.getId() + " не существует");

                return new ResponseEntity<>("Фильма с ID " + film.getId() + " не существует", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            log.error("данные не прошли валидацию");

            return new ResponseEntity<>("Некорректные данные", HttpStatus.BAD_REQUEST);
        }
    }

    private int generateID() {
        id ++;
        return id;
    }

    public boolean validate(Film film) {
        return film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)) &&
                film.getDuration() > 0;
    }
}
