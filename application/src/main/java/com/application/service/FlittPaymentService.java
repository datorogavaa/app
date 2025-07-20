package com.application.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class FlittPaymentService {

    @Value("${flitt.merchant-id}")
    private long merchantId;

    @Value("${flitt.secret-key}")
    private String secretKey;

    @Value("${flitt.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> createPaymentToken(String orderId, BigDecimal amount, String currency, String callbackUrl) {
        int amountInMinor = amount.multiply(BigDecimal.valueOf(100)).intValue(); // Convert to smallest unit

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("server_callback_url", callbackUrl);
        requestBody.put("order_id", orderId);
        requestBody.put("currency", currency);
        requestBody.put("merchant_id", merchantId);
        requestBody.put("order_desc", "Order for " + orderId);
        requestBody.put("amount", amountInMinor);

        // Compute signature
        String signature = computeSignature(requestBody);
        requestBody.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> rootRequest = new HashMap<>();
        rootRequest.put("request", requestBody);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(rootRequest, headers);

        String url = baseUrl + "/api/checkout/url";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to create payment token with Flitt");
        }
    }

    private String computeSignature(Map<String, Object> params) {
        // Flitt's documentation probably requires a specific order or concatenation
        // Example logic (you must adjust based on the official docs!):
        StringBuilder raw = new StringBuilder();

        raw.append("amount=").append(params.get("amount"));
        raw.append("&currency=").append(params.get("currency"));
        raw.append("&merchant_id=").append(params.get("merchant_id"));
        raw.append("&order_id=").append(params.get("order_id"));
        raw.append("&server_callback_url=").append(params.get("server_callback_url"));
        raw.append("&secret_key=").append(secretKey);

        return DigestUtils.md5Hex(raw.toString());
    }
}