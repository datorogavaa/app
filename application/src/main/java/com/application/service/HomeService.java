package com.application.service;

import com.application.model.Home;
import com.application.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class HomeService {


    private HomeRepository homeRepository;

    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }
    public void addHome(Home home) {
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
