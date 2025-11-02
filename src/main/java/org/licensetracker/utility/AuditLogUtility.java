package org.licensetracker.utility;

import org.licensetracker.dto.AuditLogResponseDTO;
import org.licensetracker.entity.AuditLog;

public class AuditLogUtility {

    public static AuditLogResponseDTO toDto(AuditLog auditLog) {
        return AuditLogResponseDTO.builder()
                .userId(auditLog.getUser() != null ? auditLog.getUser().getUserId() : null)
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .action(auditLog.getAction())
                .timestamp(auditLog.getTimestamp())
                .details(auditLog.getDetails())
                .build();
    }
}
