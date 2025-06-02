package com.application.controller;

import com.application.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.application.model.Home;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/homes")
public class HomeController {


    @Autowired
    private HomeService homeService;

//
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createHome(
            @RequestPart("home") Home home,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        homeService.addHome(home, images);
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


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public String editHome(@PathVariable Long id, @RequestBody Home home) {
        homeService.editHome(id,home);
        return "Home Edited Successfuly";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteHome(@PathVariable Long id) {
        homeService.deleteHome(id);
        return "Home deleted successfully";
    }








    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/rent")
    public ResponseEntity<?> rentHome(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String keycloakUserId = ((Jwt) authentication.getPrincipal()).getClaim("sub");

        try {
            homeService.linkHomeToUser(id, keycloakUserId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "status", "linked",
                "userId", keycloakUserId,
                "homeId", id
        ));
    }

}


