package com.application.service;

import com.application.dto.HomeDTO;
import com.application.mapper.HomeMapper;
import com.application.model.Home;
import com.application.model.User;
import com.application.repository.HomeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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


    Logger logger = LogManager.getLogger(HomeService.class);

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
            logger.info("Images uploaded successfully, keys: {}", imageKeys);
            home.setImageKeys(imageKeys);
        }
        logger.info("Saving home to repository with image URLs: {}", home.getImageKeys());
        homeRepository.save(home);
        logger.info("Home saved successfully with ID: {}", home.getId());
        return homeDTO;
    }

    public List<HomeDTO> allHomes() {
        List<Home> homes = homeRepository.findAll();
        List<HomeDTO> homeDTOs = new ArrayList<>();
        for (Home home : homes) {
            HomeDTO homeDTO = homeMapper.toDto(home);
            if (home.getImageKeys() != null && !home.getImageKeys().isEmpty()) {
                List<String> signedUrls = home.getImageKeys().stream()
                        .map(key -> s3Utils.generatePresignedUrl(key, 60))
                        .toList();
                homeDTO.setSignedImageUrls(signedUrls);
            }
            homeDTOs.add(homeDTO);
        }
        logger.info("Total homes fetched: {}", homeDTOs.size());
        return homeDTOs;
    }

    public HomeDTO getHomeById(Long id) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found"));
        HomeDTO homeDTO = homeMapper.toDto(home);

        if (home.getImageKeys() != null && !home.getImageKeys().isEmpty()) {
            List<String> signedUrls = home.getImageKeys().stream()
                    .map(key -> s3Utils.generatePresignedUrl(key, 60))
                    .toList();
            homeDTO.setSignedImageUrls(signedUrls);
        }
        logger.info("Home fetched successfully: {}", homeDTO.toString());
        return homeDTO;
    }


    public void editHome(Long id, HomeDTO homeDTO, List<MultipartFile> images) {
        logger.info("Editing home with ID: {}", id);
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        if (images != null && !images.isEmpty()) {
            if (home.getImageKeys() != null) {
                for (String key : home.getImageKeys()) {
                    s3Service.deleteFile(key);
                    logger.info("Image deleted successfully, key: {}", key);
                }
            }
            List<String> newImageKeys = new ArrayList<>();
            for (MultipartFile image : images) {
                try {
                    String key = UUID.randomUUID() + "-" + image.getOriginalFilename();
                    String storedKey = s3Service.uploadFile(
                            key,
                            image.getInputStream(),
                            image.getSize(),
                            image.getContentType()
                    );
                    newImageKeys.add(storedKey);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }
            }
            home.setImageKeys(newImageKeys);
            logger.info("Reuploaded images for home ID: {}, new keys: {}", id, newImageKeys);
        }

        home.setDescription(homeDTO.getDescription());
        home.setAddress(homeDTO.getAddress());
        home.setPrice(homeDTO.getPrice());
        home.setDoorCode(homeDTO.getDoorCode());
        home.setOwnerName(homeDTO.getOwnerName());
        home.setOwnerNumber(homeDTO.getOwnerNumber());
        home.setBedrooms(homeDTO.getBedrooms());
        homeRepository.save(home);
        logger.info("Home with ID: {} edited successfully", id);
    }
    public void deleteHome(Long id) {
        Optional<Home> homeOptional = homeRepository.findById(id);
        if (homeOptional.isPresent()) {
            Home home = homeOptional.get();
            home.setImageKeys(null);
            homeRepository.save(home);
            homeRepository.delete(home);
            logger.info("Home with ID: {} deleted successfully", id);
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
                logger.info("User with ID: {} assigned to home with ID: {}", userId, homeId);
            }
            return "User assigned to home successfully";
        }
        return "Home not found";
    }

}
