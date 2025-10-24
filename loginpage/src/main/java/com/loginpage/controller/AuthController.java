package com.loginpage.controller;

import com.loginpage.entities.User;
import com.loginpage.security.services.AuthService;
import com.loginpage.security.services.dto.LoginRequest;
import com.loginpage.security.services.dto.LoginResponse;
import com.loginpage.security.services.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        User user = authService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getMobile()
        );
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        User user = authService.getUserByEmail(request.getEmail());

        String redirectUrl = switch (user.getRole()) {
            case ROLE_ADMIN -> "http://dashboard-service:8081/dashboard";
            case ROLE_IT_AUDITOR -> "http://reports-service:8082/reports";
            default -> "http://devices-service:8083/devices";
        };

        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name(), redirectUrl));
    }
}
