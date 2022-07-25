package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    @NotBlank
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Integer> likes = new HashSet<>();
    private Set<String> genres = new HashSet<>();
    private String rating;

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void deleteLike(int userId) {
        likes.remove(userId);
    }
}
