package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@SecurityRequirement(name = "Bearer Authentication")

public class AlertController {
    @Autowired
    private LicenseService licenseService;
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LicenseAlertDTO>> getExpiringAlerts(@RequestParam(defaultValue = "30") int days) {
        List<LicenseAlertDTO> alerts = licenseService.getExpiringLicenses(days);
        return ResponseEntity.ok(alerts);
    }
}
