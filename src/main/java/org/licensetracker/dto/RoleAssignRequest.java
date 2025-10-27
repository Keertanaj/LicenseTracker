package org.licensetracker.dto;

import lombok.Data;
import org.licensetracker.entity.Role;

@Data
public class RoleAssignRequest {
    private Role role;
}
