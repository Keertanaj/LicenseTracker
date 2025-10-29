package org.licensetracker.service;

import lombok.RequiredArgsConstructor;
import org.licensetracker.entity.Role;
import org.licensetracker.entity.User;
import org.licensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User assignRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(newRole);
        return userRepository.save(user);
    }
}
