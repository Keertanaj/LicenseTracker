package org.licensetracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogRequestDTO {
    private Long userId;
    private String entityType;
    private String entityId;
    private String action;
    private String details;
}
