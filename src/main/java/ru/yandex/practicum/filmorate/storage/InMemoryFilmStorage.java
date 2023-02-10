package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll(){
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id){
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film){
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(Long id){
        films.remove(id);
    }

}
