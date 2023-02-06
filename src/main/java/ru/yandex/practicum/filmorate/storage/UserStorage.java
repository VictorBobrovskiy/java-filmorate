package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    List<User> getUsers();

    User getUser(Long id);

    User create(User user);

    User update(User user);

    void delete(User user);

}
