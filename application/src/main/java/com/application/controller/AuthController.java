package com.application.controller;

import com.application.model.User;
import com.application.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/register")
public class AuthController {

    private final String keycloakUrl = "http://<KEYCLOAK_HOST>/auth/realms/app-realm/protocol/openid-connect/token";
    private final String keycloakAdminUrl = "http://<KEYCLOAK_HOST>/auth/admin/realms/app-realm/users";
    private final String clientId = "admin-client";
    private final String clientSecret = "<CLIENT_SECRET>";

    @Autowired
    private UserService userService;



    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody User user) throws InterruptedException {


        // 1. Get Access Token from Keycloak with client credentials
        String accessToken = getKeycloakAccessToken();

        if (accessToken == null) {
            return "Failed to get access token from Keycloak";
        }

        // 2. Create user in Keycloak
        boolean keycloakUserCreated = createKeycloakUser(user, accessToken);

        if (!keycloakUserCreated) {
            return "Failed to create user in Keycloak";
        }

        userService.addUser(user);
        return "User added successfully";
    }
    private String getKeycloakAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "grant_type=client_credentials" +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret;

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(keycloakUrl, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonObject = new JSONObject(response.getBody());
                return jsonObject.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean createKeycloakUser(User user, String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // Build user JSON body with username and password only
            Map<String, Object> userJson = new HashMap<>();
            userJson.put("username", user.getNumber());
            userJson.put("enabled", true);

            // Password credentials - must be an array
            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", user.getPassword());
            credential.put("temporary", false);

            userJson.put("credentials", new Map[]{credential});

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userJson, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(keycloakAdminUrl, entity, String.class);

            return response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.NO_CONTENT;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
