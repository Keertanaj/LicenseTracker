package org.licensetracker.service;

import org.licensetracker.dto.AuditLogResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface AuditLogService {
    List<AuditLogResponseDTO> getAuditLogs(LocalDate startDate, LocalDate endDate, Long userId, String entityType);
}
