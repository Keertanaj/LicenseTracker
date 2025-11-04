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
@RequestMapping("/vendors") // Note the plural resource path
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> addVendor(@RequestBody VendorRequestDTO request) {
        return ResponseEntity.ok(vendorService.addVendor(request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Or hasAnyRole('ADMIN', 'USER', 'AUDITOR')
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> updateVendor(@PathVariable Long id, @RequestBody VendorRequestDTO request) {
        return ResponseEntity.ok(vendorService.updateVendor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}