package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

   @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.getUsers();
    }

    @GetMapping("users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }


    @PostMapping
    public User create (@Valid @RequestBody User user)  {
        return userService.create(user);
    }

    @PutMapping
    public User update (@Valid @RequestBody User user)  {
        return userService.update(user);
    }

    @PutMapping ("/users/{id}/friends/{friendId}")
    public void addFriend (
            @PathVariable Long id,
            @PathVariable Long friendId
    )  {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping
    public void deleteFriend (
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("users/{id}/friends")
    public ArrayList<User> findAllFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable Long id,
            @PathVariable Long otherId
    ) {
        return userService.getCommonFriends(id, otherId);
    }


}


