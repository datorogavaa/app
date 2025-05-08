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
import java.util.Optional;

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
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User Deleted Successfully";
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String editUserNumber(@PathVariable long id,@RequestBody String number) throws InterruptedException {
        userService.editUserNumber( id,number);
        return "Number Changed Successfully";
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String editUserPassword(@PathVariable long id,@RequestBody String password) throws InterruptedException {
        userService.changePassword( id,password);
        return "Password Changed Successfully";
    }
}
