package org.licensetracker.utility;

import org.licensetracker.dto.UserRequestDTO;
import org.licensetracker.dto.UserResponseDTO;
import org.licensetracker.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserUtility {
    public static User toEntity(UserRequestDTO requestDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(requestDTO.getEmail())
                .name(requestDTO.getName())
                .passwordHash(passwordEncoder.encode(requestDTO.getPassword()))
                .role(requestDTO.getRole())
                .build();
    }
    public static UserResponseDTO toDto(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
