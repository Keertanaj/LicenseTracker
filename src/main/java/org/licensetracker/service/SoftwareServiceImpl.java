package org.licensetracker.service;

import org.licensetracker.dto.AuditLogRequestDTO;
import org.licensetracker.dto.SoftwareRequestDTO;
import org.licensetracker.dto.SoftwareResponseDTO;
import org.licensetracker.entity.Software;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.DeviceRepo;
import org.licensetracker.repository.SoftwareRepository;
import org.licensetracker.utility.SoftwareUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SoftwareServiceImpl implements SoftwareService {

    @Autowired
    private SoftwareRepository softwareRepository;

    @Autowired
    private DeviceRepo deviceRepository;

    @Autowired
    private AuditLogService auditLogService;

    private Map<String, Object> getUserInfoForAudit() {
        return auditLogService.getCurrentUserInfo();
    }

    @Override
    public SoftwareResponseDTO addSoftware(SoftwareRequestDTO request) {
        Software software = SoftwareUtility.toEntity(request);
        Software savedSoftware = softwareRepository.save(software);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            String entityId = String.valueOf(savedSoftware.getId());

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("SOFTWARE")
                    .entityId(entityId)
                    .action("CREATE")
                    .details("Created software: " + savedSoftware.getSoftwareName())
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for software creation: " + e.getMessage());
        }

        return SoftwareUtility.toDto(savedSoftware);
    }

    @Override
    public List<String> getAllSoftwareNames() {
        return softwareRepository.findDistinctSoftwareNames();
    }

    @Override
    public SoftwareResponseDTO updateSoftware(Integer id, SoftwareRequestDTO request) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Software not found with id: " + id));

        String oldSoftwareName = software.getSoftwareName();

        software.setSoftwareName(request.getSoftwareName());

        Software updatedSoftware = softwareRepository.save(software);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            String entityId = String.valueOf(updatedSoftware.getId());

            String updateDetails = String.format("Updated software from '%s' to '%s'",
                    oldSoftwareName, updatedSoftware.getSoftwareName());

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("SOFTWARE")
                    .entityId(entityId)
                    .action("UPDATE")
                    .details(updateDetails)
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for software update: " + e.getMessage());
        }

        return SoftwareUtility.toDto(updatedSoftware);
    }

    @Override
    public void deleteSoftware(Integer id) {
        Software existingSoftware = softwareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Software not found with id: " + id));

        softwareRepository.delete(existingSoftware);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            String entityId = String.valueOf(id);

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("SOFTWARE")
                    .entityId(entityId)
                    .action("DELETE")
                    .details("Deleted software: " + existingSoftware.getSoftwareName())
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for software deletion: " + e.getMessage());
        }
    }

    @Override
    public List<SoftwareResponseDTO> getAllSoftware() {
        return softwareRepository.findAll().stream()
                .map(SoftwareUtility::toDto)
                .collect(Collectors.toList());
    }
}