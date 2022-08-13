package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storages.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getMpa() {
        return mpaStorage.getMpa();
    }

    public Mpa getMpaById(int ratingId) {
        try {
            return mpaStorage.getMpaById(ratingId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинга с ID " + ratingId + " не существует");
        }
    }
}
