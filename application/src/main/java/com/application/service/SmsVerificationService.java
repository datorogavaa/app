package com.application.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsVerificationService {

    @Value("${twilio.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.service.sid}")
    private String MESSAGING_SERVICE_SID;

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public void sendCode(String phoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String otp = generateOTP();
        otpStorage.put(phoneNumber, otp);

        Message.creator(
                new PhoneNumber(phoneNumber),
                MESSAGING_SERVICE_SID,
                "Your OTP code is: " + otp
        ).create();
    }

    public boolean verify(String phoneNumber, String code) {
        String storedOtp = otpStorage.get(phoneNumber);
        return storedOtp != null && storedOtp.equals(code);
    }

    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}

