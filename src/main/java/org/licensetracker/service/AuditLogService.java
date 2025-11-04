package org.licensetracker.service;

import org.licensetracker.dto.AuditLogRequestDTO;
import org.licensetracker.dto.AuditLogResponseDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AuditLogService {
    Map<String, Object> getCurrentUserInfo();
    void createLog(AuditLogRequestDTO requestDTO);
    List<AuditLogResponseDTO> getAuditLogs(LocalDate startDate, LocalDate endDate, Long userId, String entityType);
}
