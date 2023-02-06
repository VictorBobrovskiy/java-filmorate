package ru.yandex.practicum.filmorate;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTests {

    @Autowired
    InMemoryUserStorage userStorage;

    @Test
    void validateFilm() {
        FilmController filmController = new FilmController();

        Film film = new Film("Titanic", "That Titanic with DiCaprio",
                LocalDate.of(1998, 11, 25), 121);

        filmController.create(film);

        Film filmName = new Film(" ", "That titanic",
                LocalDate.of(1998, 11, 25), 121);

        try {
            filmController.create(filmName);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        Film filmDescription = new Film("Titanic", "That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic",
                LocalDate.of(1998, 11, 25), 121);
        try {
            filmController.create(filmDescription);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        Film filmReleaseDate = new Film("Titanic", "That Titanic with DiCaprio",
                LocalDate.of(1895, 12, 27), 121);
        try{
            filmController.create(filmReleaseDate);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        Film filmDuration = new Film("Titanic", "That Titanic with DiCaprio",
                LocalDate.of(1998, 11, 25), 0);
        try{
            filmController.create(filmDuration);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        Film filmBlank = new Film();
        try{
            filmController.create(filmBlank);
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }

        List<Film> filmList = filmController.findAll();

        assertEquals(1, film.getId());
        assertEquals(1, filmList.size());

    }
    @Test
    void validateUser() {

        User user = new User("user@mail.ru", "login", "name",
                LocalDate.of(1998, 11, 25));

        userStorage.create(user);

        User userName = new User("user@mail.ru", "userName", "",
                LocalDate.of(1998, 11, 25));
        userStorage.create(userName);

        User userEmail = new User("mail.ru", "userEmail", "name",
                LocalDate.of(1998, 11, 25));
        try {
            userStorage.create(userEmail);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        User userLogin = new User("user@mail.ru", " ", "userLogin",
                LocalDate.of(1998, 11, 25));
        try{
            userStorage.create(userLogin);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        User userBirthday = new User("user@mail.ru", "login", "name",
                LocalDate.now().plusDays(1));
        try{
            userStorage.create(userBirthday);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        User userBlank = new User();
        try{
            userStorage.create(userBlank);
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }

        Collection<User> userList = userStorage.getUsers();

        System.out.println(userList);
        assertEquals(2, userList.size());
        assertEquals("userName", userName.getName());

    }
}