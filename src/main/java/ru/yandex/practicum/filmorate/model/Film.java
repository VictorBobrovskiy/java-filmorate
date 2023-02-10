package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NotNull
@Slf4j
@Data
public class Film implements Comparable<Film>{

    private Long id;
    @NotBlank
    private String name;

    private String description;

    private int duration;

    private Date releaseDate;

    public GENRE genre;

    public MGA_RATING rating;

    Set<Long> likes;

    public Film() {
    }

    public Film(String name, String description, Date releaseDate,
                int duration, GENRE genre, MGA_RATING rating, Set<Long> likes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genre = genre;
        this.rating = rating;
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id.equals(film.id) && duration == film.duration && name.equals(film.name) && description.equals(film.description) && releaseDate.equals(film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }

    @Override
    public int compareTo(Film film) {
        return Integer.compare(film.getLikes().size(), this.getLikes().size());
    }
}
