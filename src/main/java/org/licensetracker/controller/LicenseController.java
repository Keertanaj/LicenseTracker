package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.LicenseRequestDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/licenses")
@CrossOrigin(origins = "http://localhost:3000")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @PostMapping
    public ResponseEntity<LicenseResponseDTO> addLicense(@RequestBody LicenseRequestDTO request) {
        return ResponseEntity.ok(licenseService.addLicense(request));
    }

    @GetMapping
    public ResponseEntity<List<LicenseResponseDTO>> listLicenses() {
        return ResponseEntity.ok(licenseService.listLicenses());
    }

    @PutMapping("/{licenseKey}")
    public ResponseEntity<LicenseResponseDTO> updateLicense(
            @PathVariable String licenseKey,
            @RequestBody LicenseRequestDTO request) {
        return ResponseEntity.ok(licenseService.updateLicense(licenseKey, request));
    }

    @DeleteMapping("/{licenseKey}")
    public ResponseEntity<String> deleteLicense(@PathVariable String licenseKey) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseKey));
    }

    @GetMapping("/search")
    public ResponseEntity<List<LicenseResponseDTO>> searchLicenses(
            @RequestParam(required = false) String vendor,
            @RequestParam(required = false) String softwareName) {
        return ResponseEntity.ok(licenseService.searchLicenses(vendor, softwareName));
    }

    @GetMapping("/vendors")
    public ResponseEntity<List<String>> getAllVendors() {
        return ResponseEntity.ok(licenseService.getAllVendors());
    }

    @GetMapping("/software")
    public ResponseEntity<List<String>> getAllSoftware() {
        return ResponseEntity.ok(licenseService.getAllSoftware());
    }
}