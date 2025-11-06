package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.SoftwareRequestDTO;
import org.licensetracker.dto.SoftwareResponseDTO;
import org.licensetracker.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/software")
@SecurityRequirement(name = "Bearer Authentication")

public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN', 'NETWORK_ENGINEER', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<SoftwareResponseDTO> addSoftware(@RequestBody SoftwareRequestDTO request) {
        return ResponseEntity.ok(softwareService.addSoftware(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECURITY_HEAD', 'PRODUCT_OWNER', 'COMPLIANCE_LEAD', 'COMPLIANCE_OFFICER', 'PROCUREMENT_LEAD', 'PROCUREMENT_OFFICER', 'OPERATIONS_MANAGER', 'IT_AUDITOR', 'NETWORK_ADMIN', 'NETWORK_ENGINEER')")
    public ResponseEntity<List<SoftwareResponseDTO>> getAllSoftware() {
        return ResponseEntity.ok(softwareService.getAllSoftware());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN', 'NETWORK_ENGINEER', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<SoftwareResponseDTO> updateSoftware(@PathVariable Integer id, @RequestBody SoftwareRequestDTO request) {
        return ResponseEntity.ok(softwareService.updateSoftware(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN', 'NETWORK_ENGINEER', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<Void> deleteSoftware(@PathVariable Integer id) {
        softwareService.deleteSoftware(id);
        return ResponseEntity.noContent().build();
    }
}
