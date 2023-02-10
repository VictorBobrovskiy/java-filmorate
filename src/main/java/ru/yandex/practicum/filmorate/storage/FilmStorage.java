package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage{

    List<Film> findAll();

    Film getFilm(Long id);

    Film create(Film film);

    Film update(Film film);

    void delete(Long id);
}
