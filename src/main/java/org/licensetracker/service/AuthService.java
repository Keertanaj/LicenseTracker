package org.licensetracker.service;

import org.licensetracker.dto.SignupRequest;
import org.licensetracker.entity.User;

public interface AuthService {
    User registerUser(SignupRequest request);
    String login(String email, String password);
    User getUserByEmail(String email);
}
