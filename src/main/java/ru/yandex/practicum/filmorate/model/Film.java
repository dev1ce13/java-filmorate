package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private LinkedHashSet<Genre> genres;
    private Mpa mpa;
}
