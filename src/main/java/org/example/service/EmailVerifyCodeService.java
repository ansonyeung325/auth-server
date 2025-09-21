package org.example.service;

import org.example.model.EmailVerifyCode;
import org.example.repository.EmailVerifyCodeRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerifyCodeService {

    @Autowired
    private EmailVerifyCodeRepository emailVerifyCodeRepository;

    public void createEmailVerifyCode(String username, String code) {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);
        EmailVerifyCode newEmailVerifyCode = new EmailVerifyCode(username, code, expiredAt);
        try {
            emailVerifyCodeRepository.save(newEmailVerifyCode);
        } catch (DataIntegrityViolationException e) {
            // Check if the cause is a duplicate key violation
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new IllegalArgumentException("Code already exists for this user");
            }
            throw e;
        }
    }

    public Optional<EmailVerifyCode> getVerifyCode(String username, String code) {
        LocalDateTime now = LocalDateTime.now();
        return emailVerifyCodeRepository.findByEmailAndCodeAndExpiredAtGreaterThanAndVerifiedIsFalse(username, code, now);
    }
}
