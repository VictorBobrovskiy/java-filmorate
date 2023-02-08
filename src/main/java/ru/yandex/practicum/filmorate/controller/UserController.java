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
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") String id) {
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

    @PutMapping ("/{id}/friends/{friendId}")
    public void addFriend (
            @PathVariable("id") String id,
            @PathVariable("friendId") String friendId
    )  {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend (
            @PathVariable("id") String id,
            @PathVariable("friendId") String friendId
    ) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public ArrayList<User> findAllFriends(@PathVariable("id") String id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable("id") String id,
            @PathVariable("otherId") String otherId
    ) {
        return userService.getCommonFriends(id, otherId);
    }

}


