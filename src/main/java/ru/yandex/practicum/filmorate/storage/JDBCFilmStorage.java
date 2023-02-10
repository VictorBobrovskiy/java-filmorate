package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class JDBCFilmStorage implements FilmStorage {
    private static final String URL = "jdbc:postgresql://localhost:5432/filmorate";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "/.,";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM films");
            while (resultSet.next()) {
                Film film = new Film();

                film.setId(resultSet.getLong("id"));
                film.setName(resultSet.getString("name"));
                film.setDescription(resultSet.getString("description"));
                film.setDuration(resultSet.getInt("duration"));
                film.setReleaseDate(resultSet.getDate("release_date"));
                film.setLikes(new HashSet<Long>());

                films.add(film);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO films VALUES(?,?,?,?,?)");

            statement.setLong(1, film.getId());
            statement.setString(2, film.getName());
            statement.setString(3, film.getDescription());
            statement.setInt(4, film.getDuration());
            statement.setDate(5, film.getReleaseDate());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return film;
    }


    @Override
    public Film getFilm(Long id){
        Film film = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM films WHERE id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            film.setId(resultSet.getLong("id"));
            film.setName(resultSet.getString("name"));
            film.setDescription(resultSet.getString("description"));
            film.setDuration(resultSet.getInt("duration"));
            film.setReleaseDate(resultSet.getDate("release_date"));
            film.setLikes(new HashSet<Long>());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return film;
    }



    @Override
    public Film update(Film film) {
        try {
            PreparedStatement statement = connection.prepareStatement
                    ("UPDATE Film SET id=?, name=?, description=?, duration=?, releasedate=?");

            statement.setLong(1, film.getId());
            statement.setString(2, film.getName());
            statement.setString(3, film.getDescription());
            statement.setInt(4, film.getDuration());
            statement.setDate(5, film.getReleaseDate());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return film;
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM films WHERE id=?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




}
