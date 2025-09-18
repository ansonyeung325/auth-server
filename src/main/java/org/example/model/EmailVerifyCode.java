package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "userId_code", columnNames = {"userId", "code"}))
public class EmailVerifyCode {
    @Id
    @UuidGenerator
    private UUID id;
    private String code;
    private UUID userId;
    private LocalDateTime expiredAt;
    private boolean verified = false;

    public EmailVerifyCode() {
    }
    public EmailVerifyCode(UUID userId, String code, LocalDateTime expiredAt) {
        this.userId = userId;
        this.code = code;
        this.expiredAt = expiredAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}
