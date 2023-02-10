package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class JBDCUserStorage implements UserStorage{

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
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setLogin(resultSet.getString("login"));
                    user.setName(resultSet.getString("name"));
                    user.setBirthday(resultSet.getDate("birthday"));
                    user.setFriends(new HashSet<Long>());
                    users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User create(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES(?,?,?,?,?)");

            statement.setLong(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getName());
            statement.setDate(5, user.getBirthday());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public User getUser(Long id){
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            user = new User();
            user.setId(resultSet.getLong("id"));
            user.setEmail(resultSet.getString("email"));
            user.setLogin(resultSet.getString("login"));
            user.setName(resultSet.getString("name"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setFriends(new HashSet<Long>());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }



    @Override
    public User update(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement
                    ("UPDATE User SET id=?, email=?, login=?, name=?, birthday=?");

            statement.setLong(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getName());
            statement.setDate(5, user.getBirthday());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id=?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
