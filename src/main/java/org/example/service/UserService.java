package org.example.service;

import org.example.model.EmailVerifyCode;
import org.example.model.User;
import org.example.repository.EmailVerifyCodeRepository;
import org.example.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerifyCodeRepository emailVerifyCodeRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByUserId(UUID userId) {
        return userRepository.findById(userId);
    }

    public void setUserEmailVerified(User user, EmailVerifyCode emailVerifyCode) {
        emailVerifyCode.setVerified(true);
        user.setEmailVerified(true);
        emailVerifyCodeRepository.save(emailVerifyCode);
        userRepository.save(user);
    }

}
