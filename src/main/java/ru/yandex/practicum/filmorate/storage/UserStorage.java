package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;


public interface UserStorage {

    List<User> getUsers();

    User getUser(Long id);

    User create(User user);

    User update(User user);

    void delete(User user);

}
