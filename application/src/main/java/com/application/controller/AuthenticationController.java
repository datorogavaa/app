package com.application.controller;

import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.security.JwtUtil;
import com.application.security.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersDetailsService usersDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getNumber(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
        final UserDetails userDetails = usersDetailsService.loadUserByUsername(request.getNumber().toString());
        final String jwt = jwtUtil.generateToken(userDetails);
        return new JwtResponse(jwt);
    }


    @PostMapping("/register")
    public String register(@RequestBody JwtRequest request) {
        if (userRepository.findByNumber(request.getNumber()).isPresent()) {
            return "User already exists";
        }
        User user = new User();
        user.setNumber(request.getNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "Registration successful";
    }


    public static class JwtRequest {
        private Integer number;
        private String password;
        // getters and setters
        public Integer getNumber() { return number; }
        public void setNumber(Integer number) { this.number = number; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class JwtResponse {
        private String token;
        public JwtResponse(String token) { this.token = token; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}