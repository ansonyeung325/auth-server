package org.example.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class VerificationCodeGenerator {

    public String generate() {
        SecureRandom secureRandom = new SecureRandom();
        int code = secureRandom.nextInt(900000) + 100000;
        System.out.println("Code generated: " + code);
        return Integer.toString(code);
    }
}
