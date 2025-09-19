package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "username_code", columnNames = {"username", "code"}))
public class EmailVerifyCode {
    @Id
    @UuidGenerator
    private UUID id;
    private String code;
    private String username;
    private LocalDateTime expiredAt;
    private boolean verified = false;

    public EmailVerifyCode() {
    }

    public EmailVerifyCode(String username, String code, LocalDateTime expiredAt) {
        this.username = username;
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

    public String getUserId() {
        return username;
    }

    public void setUserId(String username) {
        this.username = username;
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
