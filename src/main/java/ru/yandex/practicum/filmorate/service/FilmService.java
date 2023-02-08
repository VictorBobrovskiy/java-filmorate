package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;

    private static Long id = 0L;

    private Set<Long> allIds;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        allIds = new HashSet<>();
    }

    public List<Film> getFilms() {
        log.debug("Current number of films: {}", filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        validateFilm(film);
        film.setId(++id);
        allIds.add(id);
        log.debug("Film created: " + film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (!(allIds.contains(film.getId()))) {
            throw new UserNotFoundException("Film not found");
        }
        validateFilm(film);
        filmStorage.update(film);
        log.debug("Film updated: " + film);
        return film;
    }

    public Film getFilm(String idString) {
        Long id = Long.parseLong(idString);
        if (!(allIds.contains(id))) {
            throw new UserNotFoundException("Film not found");
        }
        log.debug("Film: " + filmStorage.getFilm(id));
        return filmStorage.getFilm(id);
    }

    public List<Film> getPopular(String countString) {
        int count = Integer.parseInt(countString);
        List<Film> popularFilms = filmStorage.getFilms().stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
        log.debug("The most popular films are: " + popularFilms);
        return popularFilms;
    }

    public void like(String idString, String userIdString) {
        Long id = Long.parseLong(idString);
        Long userId = Long.parseLong(userIdString);
        if (!(allIds.contains(id))) {
            throw new UserNotFoundException("Film not found");
        }
        if (filmStorage.getFilm(id).getLikes().contains(userId)) {
            throw new UserNotFoundException("User already liked the film");
        }
        Set<Long> likes = filmStorage.getFilm(id).getLikes();
        likes.add(userId);
        log.debug("Current number of likes for the current film: {}", likes.size());
    }

    public void deleteLike(String idString, String userIdString) {
        Long id = Long.parseLong(idString);
        Long userId = Long.parseLong(userIdString);
        if (!(allIds.contains(id))) {
            throw new UserNotFoundException("Film not found");
        }
        if (!(filmStorage.getFilm(id).getLikes().contains(userId))) {
            throw new UserNotFoundException("Like from this user not found");
        }
        Set<Long> likes = filmStorage.getFilm(id).getLikes();
        likes.remove(userId);
        log.debug("Like removed from user: {}", userId);
    }

    public void validateFilm(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Film name should not be blank");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film release date not be earlier than 28-12-1895. Now: " + film.getReleaseDate());
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Film description should not be more than 200 symbols");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration should not be more less than 1 minute");
        }
    }

}
