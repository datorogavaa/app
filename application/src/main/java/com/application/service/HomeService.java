package com.application.service;

import com.application.model.Home;
import com.application.repository.HomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final S3Service s3Service;

    public HomeService(HomeRepository homeRepository, S3Service s3Service) {
        this.homeRepository = homeRepository;
        this.s3Service = s3Service;
    }

    public void addHome(Home home, List<MultipartFile> images) {
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();

            for (MultipartFile image : images) {
                try {
                    String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
                    String url = s3Service.uploadFile(
                            fileName,
                            image.getInputStream(),
                            image.getSize(),
                            image.getContentType()
                    );
                    imageUrls.add(url);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }
            }

            home.setImageUrlList(imageUrls); // this stores the comma-separated string in DB
        }

        homeRepository.save(home);
    }

    public List<Home> allHomes() {
        return homeRepository.findAll();
    }

    public Optional<Home> getHomeById(Long id) {
        return homeRepository.findById(id);
    }

    public void editHome(Long id, Home newHomeData) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found"));
        home.setPostName(newHomeData.getPostName());
        home.setDescription(newHomeData.getDescription());
        home.setAddress(newHomeData.getAddress());
        home.setPrice(newHomeData.getPrice());
        home.setCode(newHomeData.getCode());
        homeRepository.save(home);
    }

    public void deleteHome(Long id) {
        homeRepository.deleteById(id);
    }
}
