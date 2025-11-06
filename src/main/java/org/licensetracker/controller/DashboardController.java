package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.DashboardMetricsDTO;
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@SecurityRequirement(name = "Bearer Authentication")

public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    private static final String READ_DEVICE_ROLES =
            "hasAnyRole('ADMIN', 'SECURITY_HEAD', 'PRODUCT_OWNER', 'COMPLIANCE_LEAD', 'COMPLIANCE_OFFICER', 'PROCUREMENT_LEAD', 'PROCUREMENT_OFFICER', 'OPERATIONS_MANAGER', 'IT_AUDITOR', 'NETWORK_ADMIN', 'NETWORK_ENGINEER')";

    @GetMapping("/metrics")
    @PreAuthorize(READ_DEVICE_ROLES)
    public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }

    @GetMapping("/expiringlicenses")
    @PreAuthorize(READ_DEVICE_ROLES)
    public ResponseEntity<List<LicenseAlertDTO>> getExpiringLicenses(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getExpiringLicenses(days));
    }

    @GetMapping("/devicesatrisk")
    @PreAuthorize(READ_DEVICE_ROLES)
    public ResponseEntity<Long> getDevicesAtRisk(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getDevicesAtRisk(days));
    }
}
