package com.application.controller;

import com.application.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.application.model.Home;

import java.util.List;


@RestController
@RequestMapping("/homes")
public class HomeController {


    @Autowired
    private HomeService homeService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String  createHome(@RequestBody Home home) {
        System.out.println(home);
        homeService.addHome(home);
        return "Home added successfully";
    }

    @GetMapping
    public List<Home> allHomes() {
        return homeService.allHomes();
    }
}
