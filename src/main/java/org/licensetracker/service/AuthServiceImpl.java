package org.licensetracker.service;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.licensetracker.dto.SignupRequest;
import org.licensetracker.entity.Role;
import org.licensetracker.entity.User;
import org.licensetracker.exception.EmailAlreadyExistsException;
import org.licensetracker.jwt.JwtUtils;
import org.licensetracker.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public User registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already taken!");
        }

        User user = new User();
        user.setName(request.getUsername()); // Correctly set the name
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER); // Default role

        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtUtils.generateJwtTokenWithRole(user.getEmail(), user.getRole().name());
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
