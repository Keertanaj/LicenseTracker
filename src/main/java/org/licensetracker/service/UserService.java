package org.licensetracker.service;

import com.loginpage.entities.Role;
import com.loginpage.entities.User;

public interface UserService {
    User assignRole(Long userId, Role newRole);
}