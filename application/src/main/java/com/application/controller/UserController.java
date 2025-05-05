package com.application.controller;

import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody User user) throws InterruptedException {
        userService.addUser(user);
        return "User Added sucessfuly";
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userService.allUsers();
    }
}
