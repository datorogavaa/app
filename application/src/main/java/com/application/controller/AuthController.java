package com.application.controller;

import com.application.dto.LoginRequest;
import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.security.JwtUtil;
import com.application.smsverification.SmsVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Step 1: Check if number exists
        User user = userRepository.findByNumber(request.getNumber()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Step 2: Send code and verify using console input
        SmsVerification smsVerification = new SmsVerification(request.getNumber());
        boolean verified = smsVerification.sendCode(); // internally uses Scanner

        if (!verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or failed verification");
        }

        // Step 3: Return JWT
        String token = jwtUtil.generateToken(user.getNumber());
        return ResponseEntity.ok(token);
    }
}
