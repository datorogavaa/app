package com.application.controller;

import com.application.dto.HomeDTO;
import com.application.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.application.model.Home;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/homes")
public class HomeController {


    @Autowired
    private HomeService homeService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createHome(
            @RequestPart("home") HomeDTO homeDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        homeService.addHome(homeDTO, images);
        return "Home added successfully";
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HomeDTO> allHomes() {
        return homeService.allHomes();
    }



    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HomeDTO getHome(@PathVariable Long id) {
        return homeService.getHomeById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public String editHome(
            @PathVariable Long id,
            @RequestPart("home") HomeDTO homeDTO,
            @RequestPart(value = "images") List<MultipartFile> images
    ) {
        homeService.editHome(id, homeDTO, images);
        return "Home Edited Successfuly";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteHome(@PathVariable Long id) {
        homeService.deleteHome(id);
        return "Home deleted successfully";
    }


    @PostMapping("/{homeId}/assign-user/{userId}")
    public ResponseEntity<String> assignUserToHome(@PathVariable Long homeId, @PathVariable Long userId) {
        String result = homeService.assignUserToHome(homeId, userId);
        if ("User assigned to home successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}


