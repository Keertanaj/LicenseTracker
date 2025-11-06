package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.VendorRequestDTO;
import org.licensetracker.dto.VendorResponseDTO;
import org.licensetracker.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
@SecurityRequirement(name = "Bearer Authentication")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_LEAD', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<VendorResponseDTO> addVendor(@RequestBody VendorRequestDTO request) {
        return ResponseEntity.ok(vendorService.addVendor(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'SECURITY_HEAD', 'COMPLIANCE_LEAD', 'COMPLIANCE_OFFICER', 'PROCUREMENT_LEAD', 'PROCUREMENT_OFFICER', 'OPERATIONS_MANAGER', 'IT_AUDITOR', 'NETWORK_ADMIN', 'NETWORK_ENGINEER')")
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_LEAD', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<VendorResponseDTO> updateVendor(@PathVariable Long id, @RequestBody VendorRequestDTO request) {
        return ResponseEntity.ok(vendorService.updateVendor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_LEAD', 'PROCUREMENT_OFFICER')")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}