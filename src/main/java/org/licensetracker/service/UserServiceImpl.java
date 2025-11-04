package org.licensetracker.service;

import org.licensetracker.dto.UserResponseDTO;
import org.licensetracker.entity.Role;
import org.licensetracker.entity.User;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.UserRepository;
import org.licensetracker.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO assignRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setRole(role);
        User updatedUser = userRepository.save(user);

        return UserUtility.toDto(updatedUser);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserUtility.toDto(user);
    }
}


