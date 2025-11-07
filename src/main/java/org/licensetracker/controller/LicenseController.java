package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.LicenseRequestDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/licenses")

public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<LicenseResponseDTO> addLicense(@RequestBody LicenseRequestDTO request) {
        return ResponseEntity.ok(licenseService.addLicense(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN','PROCUREMENT_OFFICER')")
    public ResponseEntity<List<LicenseResponseDTO>> listLicenses() {
        return ResponseEntity.ok(licenseService.listLicenses());
    }

    @PutMapping("/{licenseKey}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<LicenseResponseDTO> updateLicense(
            @PathVariable String licenseKey,
            @RequestBody LicenseRequestDTO request) {
        return ResponseEntity.ok(licenseService.updateLicense(licenseKey, request));
    }

    @DeleteMapping("/{licenseKey}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<String> deleteLicense(@PathVariable String licenseKey) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseKey));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<List<LicenseResponseDTO>> searchLicenses(
            @RequestParam(required = false) String vendor,
            @RequestParam(required = false) String softwareName) {
        return ResponseEntity.ok(licenseService.searchLicenses(vendor, softwareName));
    }

    @GetMapping("/vendors")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PROCUREMENT_OFFICER', 'COMPLIANCE_OFFICER', 'IT_AUDITOR')")
    public ResponseEntity<List<String>> getAllVendors() {
        return ResponseEntity.ok(licenseService.getAllVendors());
    }

    @GetMapping("/software")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PROCUREMENT_OFFICER', 'COMPLIANCE_OFFICER', 'IT_AUDITOR')")
    public ResponseEntity<List<String>> getAllSoftware() {
        return ResponseEntity.ok(licenseService.getAllSoftware());
    }
}