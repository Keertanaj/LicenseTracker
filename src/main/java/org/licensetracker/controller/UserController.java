package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.licensetracker.dto.UserRequestDTO;
import org.licensetracker.dto.UserResponseDTO;
import org.licensetracker.entity.Role;
import org.licensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer Authentication")

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY_HEAD')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY_HEAD')")
    public ResponseEntity<UserResponseDTO> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY_HEAD')")
    public ResponseEntity<UserResponseDTO> assignRole(@PathVariable Long userId, @RequestBody Role role) {
        return ResponseEntity.ok(userService.assignRole(userId, role));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY_HEAD')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
        UserResponseDTO newUser = userService.createUser(userRequest);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY_HEAD')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDTO userRequest) {
        UserResponseDTO updatedUser = userService.updateUser(userId, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY_HEAD')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
