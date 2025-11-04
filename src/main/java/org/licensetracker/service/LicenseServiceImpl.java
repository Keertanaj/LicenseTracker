package org.licensetracker.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.licensetracker.dto.AuditLogRequestDTO; // 👈 Import Audit DTO
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.dto.LicenseRequestDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.entity.License;
import org.licensetracker.entity.Vendor;
import org.licensetracker.exception.DuplicateResourceException;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.LicenseRepository;
import org.licensetracker.repository.VendorRepository;
import org.licensetracker.utility.LicenseUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map; // 👈 Import Map
import java.util.stream.Collectors;

@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    private LicenseRepository licenseRepo;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> getUserInfoForAudit() {
        return auditLogService.getCurrentUserInfo();
    }

    @Override
    public LicenseResponseDTO addLicense(LicenseRequestDTO request) {
        if (licenseRepo.existsById(request.getLicenseKey())) {
            throw new DuplicateResourceException("License already exists: " + request.getLicenseKey());
        }
        License license = LicenseUtility.toEntity(request, vendorRepository);
        License saved = licenseRepo.save(license);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("LICENSE")
                    .entityId("2")
                    .action("CREATE")
                    .details("CREATED LICENSE")
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for license creation: " + e.getMessage());
        }

        return LicenseUtility.toDto(saved);
    }

    @Override
    public List<LicenseResponseDTO> listLicenses() {
        return licenseRepo.findAll()
                .stream()
                .map(LicenseUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LicenseResponseDTO updateLicense(String licenseKey, LicenseRequestDTO request) {
        License license = licenseRepo.findById(licenseKey)
                .orElseThrow(() -> new ResourceNotFoundException("License not found: " + licenseKey));

        LicenseUtility.updateEntityFromDto(license, request, vendorRepository);
        License saved = licenseRepo.save(license);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("LICENSE")
                    .entityId("2")
                    .action("UPDATE")
                    .details("UPDATED LICENSE")
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for license update: " + e.getMessage());
        }

        return LicenseUtility.toDto(saved);
    }

    @Override
    public String deleteLicense(String licenseKey) {
        License license = licenseRepo.findById(licenseKey)
                .orElseThrow(() -> new ResourceNotFoundException("License not found: " + licenseKey));
        licenseRepo.delete(license);
        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("LICENSE")
                    .entityId("2")
                    .action("DELETE")
                    .details("DELETED LICENSE")
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for license deletion: " + e.getMessage());
        }

        return "License deleted successfully";
    }

    @Override
    public List<LicenseResponseDTO> searchLicenses(String vendorName, String softwareName) {
        List<License> licenses;
        if (vendorName != null && !vendorName.isEmpty()) {
            Vendor vendor = vendorRepository.findByVendorNameIgnoreCase(vendorName)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorName));
            licenses = licenseRepo.findByVendor(vendor);
        } else if (softwareName != null && !softwareName.isEmpty()) {
            licenses = licenseRepo.findBySoftwareNameContainingIgnoreCase(softwareName);
        } else {
            licenses = licenseRepo.findAll();
        }
        return licenses.stream().map(LicenseUtility::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LicenseAlertDTO> getExpiringLicenses(int days) {
        LocalDate thresholdDate = LocalDate.now().plusDays(days);
        List<License> expiringLicenses = licenseRepo.findExpiringLicenses(days);
        return expiringLicenses.stream()
                .map(LicenseUtility::toAlertDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(Vendor::getVendorName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllSoftware() {
        return licenseRepo.findDistinctSoftware();
    }
}