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
            if (userService.getUserByNumber(577132378).isEmpty()) {
                User superAdmin = new User();
                superAdmin.setNumber(577132378);
                superAdmin.setRole("ADMIN");
                userService.addAdminUser(superAdmin);
            }
        };
    }
}