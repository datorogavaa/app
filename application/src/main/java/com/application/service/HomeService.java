package com.application.service;

import com.application.model.Home;
import com.application.repository.HomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HomeService {
    private HomeRepository homeRepository;

    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }
    public List<Home> allHomes() {
        return homeRepository.findAll();
    }
    public void addHome(Home home) {
        homeRepository.save(home);
    }
    public void deleteHome(Home home) {
        homeRepository.deleteById(home.getId());
    }
    public Optional<Home> getHomeById(Long id) {
        return homeRepository.findById(id);
    }


//    public Home editHome(Home home) {
//        return home;
//    }

}
