package com.application.service;

import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.smsverification.SmsVerification;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) throws InterruptedException {
        String number = user.getNumber();
        SmsVerification smsVerification = new SmsVerification(number);
        if(smsVerification.sendCode()) {
            userRepository.save(user);
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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void editUser(Long id, User newUser) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User Not found"));
        String number = newUser.getNumber();
        SmsVerification smsVerification = new SmsVerification(number);
        if(smsVerification.sendCode()) {
            user.setNumber(number);
            userRepository.save(user);
        }else{
            System.out.println("Not Added");
            return;
        };
    }
}