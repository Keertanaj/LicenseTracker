package org.licensetracker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.licensetracker.entity.Role;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long userId;
    private String email;
    private String name;
    private Role role;
}
