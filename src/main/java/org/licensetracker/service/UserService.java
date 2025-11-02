package org.licensetracker.service;

import org.licensetracker.dto.UserResponseDTO;
import org.licensetracker.entity.Role;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO assignRole(Long userId, Role role);
    UserResponseDTO findByEmail(String email);
}
