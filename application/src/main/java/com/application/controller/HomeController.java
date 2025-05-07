package com.application.controller;

import com.application.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.application.model.Home;

import java.util.List;
import java.util.Optional;


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
    @ResponseStatus(HttpStatus.OK)
    public List<Home> allHomes() {
        return homeService.allHomes();
    }



    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Home> getHome(@PathVariable Long id) {
        return homeService.getHomeById(id);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String editHome(@PathVariable Long id, @RequestBody Home home) {
        homeService.editHome(id,home);
        return "Home Edited Successfuly";
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteHome(@PathVariable Long id) {
        homeService.deleteHome(id);
        return "Home deleted successfully";
    }
}


