package com.application.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("securityService")
public class SecurityService {

    public boolean isOwner(String ownerKeycloakId, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            return false;
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakUserId = jwt.getClaimAsString("sub");

        return keycloakUserId != null && keycloakUserId.equals(ownerKeycloakId);
    }
}
