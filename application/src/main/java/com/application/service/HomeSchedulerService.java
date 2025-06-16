package com.application.service;

import com.application.model.Home;
import com.application.repository.HomeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class HomeSchedulerService {

    private final HomeRepository homeRepository;

    public HomeSchedulerService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    @Transactional
    public void removeExpiredRentals() {
        List<Home> expiredHomes = homeRepository.findByRentedUntilBeforeAndUserIsNotNull(LocalDateTime.now());
        for (Home home : expiredHomes) {
            home.setUser(null);
            home.setRentedDate(null);
            home.setRentedUntil(null);
            homeRepository.save(home);
        }
    }
}