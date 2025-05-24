package com.application.service;

import com.application.model.User;
//import com.application.model.UserRole;
import com.application.repository.UserRepository;
//import com.application.repository.UserRoleRepository;
import com.application.smsverification.SmsVerification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private UserRepository userRepository;
//    private UserRoleRepository userRoleRepository;

    private BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.userRoleRepository = userRoleRepository;
    }

    public void addUser(User user) throws InterruptedException {
        String number = user.getNumber();
        SmsVerification smsVerification = new SmsVerification(number);
        if(smsVerification.sendCode()) {
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
//            UserRole userRole = new UserRole();
//            userRole.setUser(user);
//            userRole.setRole("ROLE_USER");
//            userRoleRepository.save(userRole);

        }else{
            System.out.println("Not Added");
            return;
        };

    }
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByNumber(String number) {
        return userRepository.findByNumber(number);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void editUserNumber(Long id, User newUser) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User Not found"));
        SmsVerification smsVerification = new SmsVerification(newUser.getNumber());
        if(smsVerification.sendCode()) {
            user.setNumber(newUser.getNumber());
            userRepository.save(user);
        }else{
            System.out.println("User not Edited");
            return;
        };
    }
    public void changePassword(Long id, User newUser) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User Not found"));
        user.setPassword(newUser.getPassword());
        userRepository.save(user);
    }
}