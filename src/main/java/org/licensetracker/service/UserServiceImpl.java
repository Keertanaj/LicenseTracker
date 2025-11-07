package org.licensetracker.service;

import lombok.RequiredArgsConstructor;
import org.licensetracker.dto.UserRequestDTO;
import org.licensetracker.dto.UserResponseDTO;
import org.licensetracker.entity.Role;
import org.licensetracker.entity.User;
import org.licensetracker.exception.DuplicateResourceException;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.UserRepository;
import org.licensetracker.utility.UserUtility;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserUtility.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDTO assignRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        user.setRole(role);
        User updatedUser = userRepository.save(user);
        return UserUtility.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        userRepository.findByEmail(userRequest.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("User with email " + userRequest.getEmail() + " already exists.");
        });

        User user = UserUtility.toEntity(userRequest, passwordEncoder);
        User savedUser = userRepository.save(user);

        return UserUtility.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Check if the new email is already taken by another user
        userRepository.findByEmail(userRequest.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getUserId().equals(userId)) {
                throw new DuplicateResourceException("Email " + userRequest.getEmail() + " is already in use.");
            }
        });

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());

        // Only update password if a new one is provided
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return UserUtility.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}