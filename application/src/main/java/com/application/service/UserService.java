package com.application.service;

import com.application.model.User;
import com.application.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SmsVerificationService smsVerificationService;

    public UserService(UserRepository userRepository, SmsVerificationService smsVerificationService) {
        this.userRepository = userRepository;
        this.smsVerificationService = smsVerificationService;
    }

    // ✅ Send OTP to any user (new or existing)
    public void sendOtp(Integer number) {
        String phone = "+995" + number;
        smsVerificationService.sendCode(phone);
        System.out.println("OTP sent to " + phone);
    }

    // ✅ Verify OTP and create user if they don't exist
    public void verifyAndLogin(Integer number, String code) {
        String phone = "+995" + number;
        boolean isValid = smsVerificationService.verify(phone, code);

        if (!isValid) {
            throw new RuntimeException("Invalid OTP");
        }

        // Only save if user does not exist
        if (userRepository.findByNumber(number).isEmpty()) {
            User user = new User();
            user.setNumber(number);
            user.setRole("USER");
            userRepository.save(user);
        }

        System.out.println("OTP verified and user is now authenticated.");
    }

    // Admin creation and other methods remain unchanged
    public void addAdminUser(User user) {
        userRepository.save(user);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByNumber(Integer number) {
        return userRepository.findByNumber(number);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
//    public void editUserNumber(Long id, User newUser) {
//        User user = userRepository.findById(id).orElseThrow(()
//                -> new RuntimeException("User Not found"));
//        SmsVerification smsVerification = new SmsVerification(newUser.getNumber());
//        if(smsVerification.sendCode()) {
//            user.setNumber(newUser.getNumber());
//            userRepository.save(user);
//        }else{
//            System.out.println("User not Edited");
//            return;
//        };
//    }
}