package org.licensetracker.service;

import org.licensetracker.entity.User;

public interface AuthService {
    User registerUser(String username, String email, String password, String mobile);
    String login(String email, String password);
    User getUserByEmail(String email);
}
