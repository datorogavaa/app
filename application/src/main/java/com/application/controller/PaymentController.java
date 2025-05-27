//package com.application.controller;
//
//import com.application.model.Home;
//import com.application.model.User;
//import com.application.service.HomeService;
//import com.application.service.UserService;
//import com.application.payment.PayPalClient;
//import com.paypal.http.HttpResponse;
//import com.paypal.orders.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/payment")
//public class PaymentController {
//
//    @Autowired
//    private PayPalClient payPalClient;
//
//    @Autowired
//    private HomeService homeService;
//
//    @Autowired
//    private UserService userService;
//
//    // Step 1: Create PayPal order for authenticated user
//    @PostMapping("/pay/forhome/{id}")
//    public ResponseEntity<?> createOrder(@PathVariable Long id, Authentication authentication) throws IOException {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//
//        String username = authentication.getName();
//
//        Optional<Home> homeOpt = homeService.getHomeById(id);
//        if (homeOpt.isEmpty()) {
//            return ResponseEntity.badRequest().body("Home not found");
//        }
//        Home home = homeOpt.get();
//
//        OrdersCreateRequest request = new OrdersCreateRequest();
//        request.header("prefer", "return=representation");
//
//        OrderRequest orderRequest = new OrderRequest()
//                .checkoutPaymentIntent("CAPTURE")
//                .applicationContext(new ApplicationContext()
//                        .returnUrl("http://localhost:8443/payment/forhome/" + id + "/success")
//                        .cancelUrl("http://localhost:8443/payment/cancel")
//                        .landingPage("BILLING")  // Allow card payments without PayPal login
//                        .userAction("PAY_NOW"))
//                .purchaseUnits(List.of(new PurchaseUnitRequest()
//                        .amountWithBreakdown(new AmountWithBreakdown()
//                                .currencyCode("USD")
//                                .value(String.format("%.2f", home.getPrice())))));
//
//        request.requestBody(orderRequest);
//
//        HttpResponse<Order> response = payPalClient.client().execute(request);
//
//        String approvalUrl = response.result().links().stream()
//                .filter(link -> "approve".equals(link.rel()))
//                .map(LinkDescription::href)
//                .findFirst()
//                .orElse(null);
//
//        if (approvalUrl == null) {
//            return ResponseEntity.internalServerError().body("No approval URL found");
//        }
//
//        // Return approval URL to frontend for user redirection
//        return ResponseEntity.ok(Map.of("approvalUrl", approvalUrl));
//    }
//
//    // Step 2: Capture payment after user approval on PayPal side
//    @GetMapping("/forhome/{id}/success")
//    public ResponseEntity<?> captureOrder(@RequestParam("token") String orderId, Authentication authentication, @PathVariable Long id) throws IOException {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//
//        String username = authentication.getName();
//
//        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
//        request.requestBody(new OrderRequest());
//
//        HttpResponse<Order> response = payPalClient.client().execute(request);
//
//        if (response.statusCode() == 201 || response.statusCode() == 200) {
//            Optional<User> userOpt = userService.getUserByNumber(username);
//            Optional<Home> homeOpt = homeService.getHomeById(id);
//
//            if (userOpt.isPresent() && homeOpt.isPresent()) {
//                User userEntity = userOpt.get();
//                Home home = homeOpt.get();
//
//                userEntity.getHomes().add(home);
//                home.setUser(userEntity);
//
//                userService.addUser(userEntity);
//                homeService.addHome(home);
//
//                return ResponseEntity.ok(Map.of(
//                        "status", "success",
//                        "message", "Payment captured and home added",
//                        "orderId", orderId,
//                        "home", home.getPostName()
//                ));
//            }
//            return ResponseEntity.badRequest().body("User or home not found");
//        }
//
//        return ResponseEntity.badRequest().body("Payment capture failed");
//    }
//
//    // Step 3: Payment cancellation handler
//    @GetMapping("/cancel")
//    public String cancel() {
//        return "Payment cancelled by user.";
//    }
//}
//
