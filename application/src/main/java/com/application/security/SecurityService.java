package com.application.security;

import com.application.model.User;
import com.application.security.CustomUserDetails;
import com.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("securityService")
public class


SecurityService {

    @Autowired
    private UserService userService;

    public boolean isOwner(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        String currentNumber;

        if (principal instanceof CustomUserDetails) {
            currentNumber = ((CustomUserDetails) principal).getUsername(); // or getNumber()
        } else {
            currentNumber = authentication.getName();
        }

        // Fetch the user by id
        Optional<User> userOpt = userService.getUser(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Check if current user number matches the user's number
        return user.getNumber().equals(currentNumber);
    }
}
