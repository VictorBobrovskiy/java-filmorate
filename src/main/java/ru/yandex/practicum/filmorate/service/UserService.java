package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {

    UserStorage userStorage;

    private static Long id = 0L;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public List<User> getUsers() {
        log.debug("Current number of users: {}", userStorage.getUsers().size());
        return userStorage.getUsers();
    }

    public User getUser(Long id){
        log.debug("User created: " + userStorage.getUser(id));
        return userStorage.getUser(id);
    }

    public User create (User user)  {
        if (userStorage.getUsers().contains(user)) {
            throw new UserNotFoundException("User with id " + user +" already exists");
        }
        validateUser(user);
        user.setId(++id);
        log.debug("User created: " + user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (!(userStorage.getUsers().contains(user))) {
            throw new UserNotFoundException("User with id " + user +" not found");
        }
        try {
            validateUser(user);
            userStorage.update(user);
            log.debug("User updated: " + user);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
        return user;
    }


    public void addFriend(Long id, Long friendId) {
        Set<Long> friends = userStorage.getUser(id).getFriends();
        friends.add(friendId);
        Set<Long> friendFriends = userStorage.getUser(friendId).getFriends();
        friendFriends.add(id);
        log.debug("New friend added: " + userStorage.getUser(friendId));
    }

    public void deleteFriend(Long id, Long friendId) {
        Set<Long> friends = userStorage.getUser(id).getFriends();
        friends.remove(friendId);
        Set<Long> friendFriends = userStorage.getUser(friendId).getFriends();
        friendFriends.remove(id);
        log.debug("User deleted from friends: " + userStorage.getUser(friendId));
    }

    public ArrayList<User> getFriends(Long id) {
        User user =  userStorage.getUser(id);
        Set<Long> userIds =  user.getFriends();
        ArrayList<User> friendsList = new ArrayList<>();
        for (Long friendId : userIds) {
            friendsList.add(userStorage.getUser(friendId));
        }
        log.debug("Current number of friends for the current user: {}", friendsList.size());

        return friendsList;
    }

    public ArrayList<User> getCommonFriends (Long id, Long otherId) {
        ArrayList<User> friends = getFriends(id);
        ArrayList<User> otherFriends = getFriends(otherId);
        friends.retainAll(otherFriends);
        log.debug("Current number of common friends for two users: {}", friends.size());
        return friends;
    }

    public static void validateUser(User user) {
        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("A valid e-mail should contain @");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("Invalid login, no spaces allowed");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday should be in the past");
        }
    }


    public void delete(User user) {
        if (!(userStorage.getUsers().contains(user))) {
            throw new ValidationException("User with id " + user.getId() +" not found");
        }
        userStorage.delete(user);
    }
}
