package org.licensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.licensetracker.dto.RoleAssignRequest;
import org.licensetracker.entity.User;
import org.licensetracker.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRole(@PathVariable Long userId, @RequestBody RoleAssignRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(request.getRole());
        userRepository.save(user);
        return ResponseEntity.ok("Role updated successfully");
    }


}