package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class UserService {

    UserStorage userStorage;

    private static Long id = 0L;

    private Set<Long> allIds;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
        allIds = new HashSet<>();
    }

    public Set<Long> getAllIds() {
        return allIds;
    }

    public List<User> findAll() {
        log.debug("Current number of users: {}", userStorage.findAll().size());
        return userStorage.findAll();
    }

    public User getUser(String idString){
        Long id = Long.parseLong(idString);
        if (!(allIds.contains(id))) {
            throw new UserNotFoundException("User not found");
        }
        log.debug("User: " + userStorage.getUser(id));
        return userStorage.getUser(id);
    }


    public User create (User user)  {
        validateUser(user);
        user.setId(++id);
        allIds.add(id);
        log.debug("User created: " + user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (!(allIds.contains(user.getId()))) {
            throw new UserNotFoundException("User not found");
        }
        validateUser(user);
        userStorage.update(user);
        log.debug("User updated: " + user);
        return user;
    }

    public void addFriend(String idString, String friendIdString) {
        Long id = Long.parseLong(idString);
        Long friendId = Long.parseLong(friendIdString);
        if (!(allIds.contains(id) && allIds.contains(friendId))) {
            throw new UserNotFoundException("User not found");
        }
        Set<Long> friends = userStorage.getUser(id).getFriends();
        friends.add(friendId);
        Set<Long> friendFriends = userStorage.getUser(friendId).getFriends();
        friendFriends.add(id);
        log.debug("New friend added: " + userStorage.getUser(friendId));
    }

    public void deleteFriend(String idString, String friendIdString) {
        Long id = Long.parseLong(idString);
        Long friendId = Long.parseLong(friendIdString);
        if (!(allIds.contains(id) && allIds.contains(friendId) )) {
            throw new UserNotFoundException("User not found");
        }
        Set<Long> friends = userStorage.getUser(id).getFriends();
        friends.remove(friendId);
        Set<Long> friendFriends = userStorage.getUser(friendId).getFriends();
        friendFriends.remove(id);
        log.debug("User deleted from friends: " + userStorage.getUser(friendId));
    }

    public ArrayList<User> getFriends(String idString) {
        Long id = Long.parseLong(idString);
        if (!(allIds.contains(id))) {
            throw new UserNotFoundException("User not found");
        }
        Set<Long> userIds =  userStorage.getUser(id).getFriends();
        ArrayList<User> friendsList = new ArrayList<>();
        for (Long friendId : userIds) {
            friendsList.add(userStorage.getUser(friendId));
        }
        log.debug("Current number of friends for the current user: {}", friendsList.size());

        return friendsList;
    }

    private Set<Long> getFriendsIdsFromString(String idString) {
        Long id = Long.parseLong(idString);
        if (!(allIds.contains(id))) {
            throw new UserNotFoundException("User not found");
        }
        return userStorage.getUser(id).getFriends();
    }

    public ArrayList<User> getCommonFriends (String idString, String otherIdString) {
        Long id = Long.parseLong(idString);
        Long otherId = Long.parseLong(otherIdString);
        if (!(allIds.contains(id) && allIds.contains(otherId) )) {
            throw new UserNotFoundException("User not found");
        }
        ArrayList<User> friends = getFriends(idString);
        ArrayList<User> otherFriends = getFriends(otherIdString);
        friends.retainAll(otherFriends);
        log.debug("Current number of common friends for two users: {}", friends.size());
        return friends;
    }

    public static void validateUser(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("A valid e-mail should contain @");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("Invalid login, no spaces allowed");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().after(Date.from(Instant.now()))) {
            throw new ValidationException("Birthday should be in the past");
        }
    }


    public void delete(Long id) {
        if (!(allIds.contains(id))) {
            throw new UserNotFoundException("User not found");
        }
        userStorage.delete(id);
    }
}
