package com.application;

import com.application.model.User;
import com.application.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner createSuperAdmin(UserService userService) {
        return args -> {
            if (userService.getUserByNumber(591721515).isEmpty()) {
                User superAdmin = new User();
                superAdmin.setNumber(591721515);
                superAdmin.setPassword("superpassword");
                superAdmin.setRole("ADMIN");
                userService.addUser(superAdmin);
            }
        };
    }
}