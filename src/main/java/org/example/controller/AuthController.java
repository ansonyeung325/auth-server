package org.example.controller;

import jakarta.mail.MessagingException;
import org.example.enums.JwtScope;
import org.example.model.*;
import org.example.service.EmailVerifyCodeService;
import org.example.service.UserService;
import org.example.utils.EmailSender;
import org.example.utils.JwtUtil;
import org.example.utils.VerificationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmailVerifyCodeService emailVerifyCodeService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VerificationCodeGenerator codeGenerator;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("email/get-verify-code")
    public ResponseEntity<Void> requestEmailVerifyCode(@RequestBody String email) {
        System.out.println("Receive email verify request");

        Optional<User> user = userService.getUserByUsername(email);
        if (user.isEmpty()) {
            System.out.println("User not found with email: " + email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            String code = codeGenerator.generate();
            emailSender.send(email, "Test", "This is your verify code: " + code);
            emailVerifyCodeService.createEmailVerifyCode(user.get().getUsername(), code);
        } catch (MessagingException e) {
            System.out.println("Error occurred: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("email/verify")
    public ResponseEntity<String> emailVerify(@RequestBody Map<String, String> map) {
        String code = map.get("code");
        String username = map.get("username");
        System.out.println("Receive code verify request: " + username);

        Optional<EmailVerifyCode> codeRecord = emailVerifyCodeService.getVerifyCode(username, code);
        if (codeRecord.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> user = userService.getUserByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }

        userService.setUserEmailVerified(user.get(), codeRecord.get());
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getUsername());
            return ResponseEntity.ok().body(jwtUtil.generateToken(userDetails, JwtScope.authenticated));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Internal server error");
        }
    }

    @PostMapping("sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpForm form) {
        System.out.println("Receive sign up request");
        // TODO: Check if this user already exist.
        Optional<User> existingUser = userService.getUserByUsername(form.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(702).body("User already sign up, redirect to login");
        }

        // TODO: Validate user password input
        // Minimum 8 words
        // Include at least one uppercase letter and number
        String password = form.getPassword();
        boolean valid = password.length() >= 8 && password.matches(".*[A-Z].*");

        if (!valid) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(form.getUsername(), hashedPassword);
        userService.createUser(user);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(form.getUsername());
            System.out.println("Sign up successfully");
            return ResponseEntity.ok().body(jwtUtil.generateToken(userDetails, JwtScope.email_verification));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Internal server error");
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginForm form) {
        System.out.println("Receive login request");

        Optional<User> user = userService.getUserByUsername(form.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(form.getUsername());
            JwtScope scope = user.get().isEmailVerified() ? JwtScope.authenticated : JwtScope.email_verification;

            return ResponseEntity.ok().body(jwtUtil.generateToken(userDetails, scope));
        } catch (BadCredentialsException e) {
            System.out.println("Fail to login: " + e);
            return ResponseEntity.badRequest().body("Internal server error");
        }
    }
}
