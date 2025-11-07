package org.licensetracker.service;

import org.licensetracker.dto.UserRequestDTO;
import org.licensetracker.dto.UserResponseDTO;
import org.licensetracker.entity.Role;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO findByEmail(String email);
    UserResponseDTO assignRole(Long userId, Role role);
    UserResponseDTO createUser(UserRequestDTO userRequest);
    UserResponseDTO updateUser(Long userId, UserRequestDTO userRequest);
    void deleteUser(Long userId);
}