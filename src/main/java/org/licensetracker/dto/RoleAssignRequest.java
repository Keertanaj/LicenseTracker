package org.licensetracker.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.licensetracker.entity.Role;

@Getter
@Setter
public class RoleAssignRequest {
    private Role role;
}
