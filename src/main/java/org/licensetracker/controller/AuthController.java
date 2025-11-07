package org.licensetracker.controller;


import lombok.RequiredArgsConstructor;
import org.licensetracker.dto.LoginRequest;
import org.licensetracker.dto.LoginResponse;
import org.licensetracker.dto.SignupRequest;
import org.licensetracker.entity.User;
import org.licensetracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        authService.registerUser(
                request
        );
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        User user = authService.getUserByEmail(request.getEmail());

        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name()));
    }
}
