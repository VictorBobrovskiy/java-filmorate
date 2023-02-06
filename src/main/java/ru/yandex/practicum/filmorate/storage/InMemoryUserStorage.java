package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users;


    @Autowired
    public InMemoryUserStorage(Map<Integer, User> users) {
        users = new HashMap<>();
    }

    @Override
    public List<User> getUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public User getUser(Long id){
        return users.get(id);
    }


    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }



}
