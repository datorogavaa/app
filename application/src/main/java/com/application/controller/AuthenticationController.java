package com.application.controller;

import com.application.dto.JwtRequestDTO;
import com.application.dto.OtpVerifyRequestDTO;
import com.application.dto.JwtResponseDTO;
import com.application.model.User;
import com.application.security.CustomUserDetailsService;
import com.application.security.JwtUtil;
import com.application.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthenticationController(UserService userService,
                                    CustomUserDetailsService userDetailsService,
                                    JwtUtil jwtUtil) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody JwtRequestDTO request) {
        userService.sendOtp(request.getNumber());
        return "OTP sent";
    }

    @PostMapping("/verify-otp")
    public JwtResponseDTO verifyOtp(@RequestBody OtpVerifyRequestDTO request) {
        userService.verifyAndLogin(request.getNumber(), request.getCode());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getNumber().toString());
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new JwtResponseDTO(accessToken, refreshToken);
    }




    @PostMapping("/refresh-token")
    public JwtResponseDTO refreshToken(@RequestBody JwtResponseDTO request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String username = jwtUtil.extractUsernameFromRefreshToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new JwtResponseDTO(newAccessToken, newRefreshToken);
    }


}

