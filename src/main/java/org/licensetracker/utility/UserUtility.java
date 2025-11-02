package org.licensetracker.utility;

import org.licensetracker.dto.UserResponseDTO;
import org.licensetracker.entity.User;

public class UserUtility {

    public static UserResponseDTO toDto(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
