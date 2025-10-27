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
@CrossOrigin(origins = "http://localhost:3000")
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
            case ROLE_ADMIN -> "http://localhost:3000/dashboard";
            case ROLE_IT_AUDITOR -> "http://localhost:3000/reports";
            default -> "http://localhost:3000/devices";
        };

        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name(), redirectUrl));
    }
}
