package com.application.controller;

import com.application.dto.PaymentRequestDTO;
import com.application.service.FlittPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flitt")
public class FlittController {

    @Autowired
    private FlittPaymentService flittPaymentService;

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(
                flittPaymentService.createPaymentToken(
                        dto.getOrderId(),
                        dto.getAmount(),
                        dto.getCurrency(),
                        dto.getCallbackUrl()
                )
        );
    }
}