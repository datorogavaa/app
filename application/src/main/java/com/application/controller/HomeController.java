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
import java.util.UUID;


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
        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Declare keycloakUserId outside the try block
        UUID keycloakUserId = null;

        // Retrieve the 'sub' claim from JWT token (which is the user ID)
        String keycloakUserIdStr = ((Jwt) authentication.getPrincipal()).getClaim("sub");

        try {
            // Convert the string 'sub' to UUID
            keycloakUserId = UUID.fromString(keycloakUserIdStr);

            // Now, call the service method to link the home to the user
            homeService.linkHomeToUser(id, keycloakUserId);

        } catch (IllegalArgumentException e) {
            // If 'sub' is not a valid UUID string
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format");
        } catch (RuntimeException e) {
            // If any other error occurs (e.g., Home not found or other runtime errors)
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Return success response after the try block
        return ResponseEntity.ok(Map.of(
                "status", "linked",
                "userId", keycloakUserId.toString(),  // Now 'keycloakUserId' is accessible here
                "homeId", id
        ));
    }




}


