package org.example.repository;

import org.example.model.EmailVerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerifyCodeRepository extends JpaRepository<EmailVerifyCode, UUID> {
    Optional<EmailVerifyCode> findByEmailAndCodeAndExpiredAtGreaterThanAndVerifiedIsFalse(String email, String code, LocalDateTime now);
}
