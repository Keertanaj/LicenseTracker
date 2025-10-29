package org.licensetracker.service;


import org.licensetracker.entity.Role;
import org.licensetracker.entity.User;

public interface UserService {
    User assignRole(Long userId, Role newRole);
}