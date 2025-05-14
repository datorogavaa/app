package com.application.controller;

import com.application.model.User;
import com.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/register")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody User user) throws InterruptedException {
        userService.addUser(user);
        return "User Added sucessfuly";
    }

}
