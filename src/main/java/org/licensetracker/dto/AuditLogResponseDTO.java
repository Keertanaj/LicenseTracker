package org.licensetracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogResponseDTO {
    private Long userId;
    private String entityType;
    private String entityId;
    private String action;
    private LocalDateTime timestamp;
    private String details;
}
