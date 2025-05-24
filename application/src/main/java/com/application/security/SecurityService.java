package com.application.security;

import com.application.model.User;
import com.application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("securityService")
public class SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public boolean isOwner(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            return false;
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        String number = jwt.getClaimAsString("preferred_username");

        logger.info("Token username (number): " + number);

        Optional<User> user = userService.getUserByNumber(number);
        logger.info("Resolved DB user id: " + user.map(User::getId).orElse(null));
        return user.map(u -> u.getId() == id).orElse(false);
    }
}
