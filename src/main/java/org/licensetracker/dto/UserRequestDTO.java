package org.licensetracker.dto;

import lombok.Getter;
import lombok.Setter;
import org.licensetracker.entity.Role;

@Getter
@Setter
public class UserRequestDTO {
    private String email;
    private String name;
    private String password; // Plain text password for transport
    private Role role;
}