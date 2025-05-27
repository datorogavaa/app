//package com.application.payment;
//
//import com.paypal.core.PayPalEnvironment;
//import com.paypal.core.PayPalHttpClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PayPalClient {
//
//    private PayPalHttpClient client;
//
//    public PayPalClient(
//            @Value("${paypal.client-id}") String clientId,
//            @Value("${paypal.client-secret}") String clientSecret,
//            @Value("${paypal.mode:sandbox}") String mode // sandbox or live
//    ) {
//        PayPalEnvironment environment;
//        if ("live".equalsIgnoreCase(mode)) {
//            environment = new PayPalEnvironment.Live(clientId, clientSecret);
//        } else {
//            environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
//        }
//        this.client = new PayPalHttpClient(environment);
//    }
//
//    public PayPalHttpClient client() {
//        return this.client;
//    }
//}
