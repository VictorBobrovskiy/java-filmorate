//package ru.yandex.practicum.filmorate;
//
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.service.FilmService;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.*;
//
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ValidationTests {
//
//     UserService userService = new UserService(new JBDCUserStorage());
//     FilmService filmservice = new FilmService(new JDBCFilmStorage());
//
//
//
//    @Test
//    void validateFilm() {
//
//        Film film = new Film("Titanic", "That Titanic with DiCaprio",
//                Date.valueOf("1998-11-25"), 121);
//
//        filmservice.create(film);
//
//        Film filmName = new Film(" ", "That titanic",
//                Date.valueOf("1998-11-25"), 121);
//
//        try {
//            filmservice.create(filmName);
//        } catch (ValidationException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        Film filmDescription = new Film("Titanic", "That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic with DiCaprio That Titanic",
//                Date.valueOf("1998-11-25"), 121);
//        try {
//            filmservice.create(filmDescription);
//        } catch (ValidationException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        Film filmReleaseDate = new Film("Titanic", "That Titanic with DiCaprio",
//                Date.valueOf("1895-12-27"), 121);
//        try{
//            filmservice.create(filmReleaseDate);
//        } catch (ValidationException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        Film filmDuration = new Film("Titanic", "That Titanic with DiCaprio",
//                Date.valueOf("1998-11-25"), 0);
//        try{
//            filmservice.create(filmDuration);
//        } catch (ValidationException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        Film filmBlank = new Film();
//        try{
//            filmservice.create(filmBlank);
//        } catch (NullPointerException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        List<Film> filmList = filmservice.findAll();
//
//        assertEquals(1, film.getId());
//        assertEquals(1, filmList.size());
//
//    }
//    @Test
//    void validateUser() {
//
//        User user = new User("user@mail.ru", "login", "name",
//                Date.valueOf("1998-11-25"));
//
//        userService.create(user);
//
//        User userName = new User("user@mail.ru", "userName", "",
//                Date.valueOf("1998-11-25"));
//        userService.create(userName);
//
//        User userEmail = new User("mail.ru", "userEmail", "name",
//                Date.valueOf("1998-11-25"));
//        try {
//            userService.create(userEmail);
//        } catch (ValidationException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        User userLogin = new User("user@mail.ru", " ", "userLogin",
//                Date.valueOf("1998-11-25"));
//        try{
//            userService.create(userLogin);
//        } catch (ValidationException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        User userBirthday = new User("user@mail.ru", "login", "name",
//                Date.valueOf(LocalDate.now().plusDays(1)));
//        try{
//            userService.create(userBirthday);
//        } catch (ValidationException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        User userBlank = new User();
//        try{
//            userService.create(userBlank);
//        } catch (NullPointerException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        Collection<User> userList = userService.findAll();
//
//        System.out.println(userList);
//        assertEquals(2, userList.size());
//        assertEquals("userName", userName.getName());
//
//    }
//}