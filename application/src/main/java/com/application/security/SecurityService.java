//package com.application.security;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SecurityService {
//
//    // Check if the current logged-in user is accessing their own data
//    public boolean isCurrentUser(Long id) {
//        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();  // Get the logged-in user ID (or username)
//        return currentUser.equals(id);  // Compare the current logged-in user with the userId in the URL
//    }
//}
