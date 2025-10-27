package org.licensetracker.service;

import lombok.RequiredArgsConstructor;
import org.licensetracker.entity.Role;
import org.licensetracker.entity.User;
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
    public User registerUser(String username, String email, String password, String mobile) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already taken!");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .mobile(mobile)
                .role(Role.ROLE_USER)
                .build();

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

