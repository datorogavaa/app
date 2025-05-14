package com.application.controller;

import com.application.model.Home;
import com.application.model.User;
import com.application.repository.HomeRepository;
import com.application.repository.UserRepository;
import com.application.security.CustomUserDetails;
import com.application.service.HomeService;
import com.application.service.PaypalService;
import com.application.service.UserService;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private HomeService homeService; // Inject the repository to fetch home price

    @Autowired
    private HomeRepository homeRepository; // Inject the repository to fetch home price


    @Autowired
    private UserService userService;


    @Autowired
    private UserRepository userRepository;

    // Endpoint to initiate payment for a specific Home entity
    @PostMapping("/pay/forhome/{id}")
    public String pay(@PathVariable Long id) {
        // Fetch Home entity from DB
        Optional<Home> homeOptional = homeService.getHomeById(id);

        if (!homeOptional.isPresent()) {
            return "Home not found!";
        }

        Home home = homeOptional.get();
        Double price = home.getPrice();

        try {
            // Call the service to create the payment, passing the price
            Payment payment = paypalService.createPayment(
                    price,             // Use the price from Home
                    "GEL",             // Currency (e.g., USD)
                    "paypal",          // Payment method
                    "sale",            // Intent ("sale", "authorize", "order")
                    "Payment for " + home.getPostName(),  // Description
                    "http://localhost:8080/payment/cancel",  // Cancel URL
                    "http://localhost:8080/payment/forhome/" + id + "/success" // Success URL
            );

            // Return the approval URL for the user to be redirected to PayPal
            return payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .map(link -> "Redirect the user to: " + link.getHref())
                    .findFirst()
                    .orElse("No approval link found.");

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Handle PayPal success after redirect
    @GetMapping("/forhome/{id}/success")
    public String success(@RequestParam("paymentId") String paymentId,
                          @RequestParam("PayerID") String payerId , @AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long id) {
        try {
            // Execute the payment after user approval
            Payment payment = paypalService.executePayment(paymentId,payerId);
            Optional<User> optUser = userService.getUserByNumber(customUserDetails.getUsername());
            Optional<Home> optHome = homeService.getHomeById(id);
            if(optUser.isPresent() && optHome.isPresent()) {
                User user = optUser.get();
                Home home = optHome.get();
                user.getHomes().add(home);
                home.setUser(user);
                homeRepository.save(home);
                userRepository.save(user);
                return "Payment successful: " + payment.getId();
            }
            return null;
        } catch (Exception e) {
            return "Execution failed: " + e.getMessage();
        }
    }

    // Handle cancelation
    @GetMapping("/cancel")
    public String cancel() {
        return "Payment canceled by user.";
    }
}
