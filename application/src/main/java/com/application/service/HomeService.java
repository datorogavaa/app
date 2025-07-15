package com.application.service;

import com.application.dto.HomeDTO;
import com.application.mapper.HomeMapper;
import com.application.model.Home;
import com.application.model.User;
import com.application.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final HomeMapper homeMapper;
    private final S3Service s3Service;
    private final S3Utils s3Utils;


    @Autowired
    private UserService userService;


    public HomeService(HomeRepository homeRepository, S3Service s3Service, S3Utils s3Utils, HomeMapper homeMapper) {
        this.homeRepository = homeRepository;
        this.s3Service = s3Service;
        this.s3Utils = s3Utils;
        this.homeMapper = homeMapper;
    }

    public HomeDTO addHome(HomeDTO homeDTO, List<MultipartFile> images) {
        Home home = homeMapper.toEntity(homeDTO);
        if (images != null && !images.isEmpty()) {
            List<String> imageKeys = new ArrayList<>();

            for (MultipartFile image : images) {
                try {
                    String key = UUID.randomUUID() + "-" + image.getOriginalFilename();
                    String storedKey = s3Service.uploadFile(
                            key,
                            image.getInputStream(),
                            image.getSize(),
                            image.getContentType()
                    );
                    imageKeys.add(storedKey);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }
            }

            home.setImageUrls(imageKeys);
        }

        homeRepository.save(home);
        return homeDTO;
    }

    public List<HomeDTO> allHomes() {
        List<Home> homes = homeRepository.findAll();

        List<HomeDTO> homeDTOs = new ArrayList<>();
        for (Home home : homes) {
            HomeDTO homeDTO = homeMapper.toDto(home);
            if (home.getImageUrls() != null && !home.getImageUrls().isEmpty()) {
                List<String> signedUrls = home.getImageUrls().stream()
                        .map(key -> s3Utils.generatePresignedUrl(key, 60))
                        .toList();
                homeDTO.setSignedImageUrls(signedUrls);
            }
            homeDTOs.add(homeDTO);
        }
        return homeDTOs;
    }

    public HomeDTO getHomeById(Long id) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found"));
        HomeDTO homeDTO = homeMapper.toDto(home);

        if (home.getImageUrls() != null && !home.getImageUrls().isEmpty()) {
            List<String> signedUrls = home.getImageUrls().stream()
                    .map(key -> s3Utils.generatePresignedUrl(key, 60))
                    .toList();
            homeDTO.setSignedImageUrls(signedUrls);
        }

        return homeDTO;
    }


    public void editHome(Long id, Home newHomeData) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found"));
        home.setDescription(newHomeData.getDescription());
        home.setAddress(newHomeData.getAddress());
        home.setPrice(newHomeData.getPrice());
        home.setDoorCode(newHomeData.getDoorCode());
        home.setOwnerName(newHomeData.getOwnerName());
        home.setOwnerNumber(newHomeData.getOwnerNumber());

        homeRepository.save(home);
    }
    public void deleteHome(Long id) {
        Optional<Home> homeOptional = homeRepository.findById(id);
        if (homeOptional.isPresent()) {
            Home home = homeOptional.get();
            home.setImageUrls(null);
            homeRepository.save(home);

            homeRepository.delete(home);
        }
    }


    public String assignUserToHome(Long homeId, Long userId) {
        Optional<Home> homeOptional = homeRepository.findById(homeId);
        if (homeOptional.isPresent()) {
            Home home = homeOptional.get();
            Optional<User> userOptional = userService.getUser(userId);
            if (userOptional.isEmpty()) {
                return "User not found";
            }else{
                User user = userOptional.get();
                home.setUser(user);
                home.setRentedDate(LocalDateTime.now());
                home.setRentedUntil(LocalDateTime.now().plusHours(1)); // Set rentedUntil to 24 hours later
                homeRepository.save(home);
            }
            return "User assigned to home successfully";
        }
        return "Home not found";
    }

}
