package org.licensetracker.service;

import org.licensetracker.dto.AuditLogRequestDTO;
import org.licensetracker.dto.VendorRequestDTO;
import org.licensetracker.dto.VendorResponseDTO;
import org.licensetracker.entity.Vendor;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// You will need a VendorUtility class, created in step 5, to compile this
import org.licensetracker.utility.VendorUtility;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AuditLogService auditLogService; // Assuming AuditLogService is available

    // Helper method for audit log
    private Map<String, Object> getUserInfoForAudit() {
        return auditLogService.getCurrentUserInfo();
    }

    @Override
    public VendorResponseDTO addVendor(VendorRequestDTO request) {
        Vendor vendor = VendorUtility.toEntity(request);
        Vendor savedVendor = vendorRepository.save(vendor);

        // --- Audit Logging for CREATE ---
        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            String entityId = String.valueOf(savedVendor.getVendorId());

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("VENDOR")
                    .entityId(entityId)
                    .action("CREATE")
                    .details("Created vendor: " + savedVendor.getVendorName())
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for vendor creation: " + e.getMessage());
        }

        return VendorUtility.toDto(savedVendor);
    }

    @Override
    public VendorResponseDTO updateVendor(Long vendorId, VendorRequestDTO request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));

        String oldVendorName = vendor.getVendorName();

        // Update fields
        vendor.setVendorName(request.getVendorName());
        vendor.setSupportEmail(request.getSupportEmail());

        Vendor updatedVendor = vendorRepository.save(vendor);

        // --- Audit Logging for UPDATE ---
        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            String entityId = String.valueOf(updatedVendor.getVendorId());

            String updateDetails = String.format("Updated vendor from '%s' to '%s'",
                    oldVendorName, updatedVendor.getVendorName());

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("VENDOR")
                    .entityId(entityId)
                    .action("UPDATE")
                    .details(updateDetails)
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for vendor update: " + e.getMessage());
        }

        return VendorUtility.toDto(updatedVendor);
    }

    @Override
    public void deleteVendor(Long vendorId) {
        Vendor existingVendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));

        vendorRepository.delete(existingVendor);

        // --- Audit Logging for DELETE ---
        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            String entityId = String.valueOf(vendorId);

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("VENDOR")
                    .entityId(entityId)
                    .action("DELETE")
                    .details("Deleted vendor: " + existingVendor.getVendorName())
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for vendor deletion: " + e.getMessage());
        }
    }

    @Override
    public List<VendorResponseDTO> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(VendorUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VendorResponseDTO getVendorById(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));
        return VendorUtility.toDto(vendor);
    }
}
