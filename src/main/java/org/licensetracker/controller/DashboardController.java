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

    @GetMapping("/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'COMPLIANCE_LEAD','PRODUCT_OWNER')")
    public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }

    @GetMapping("/expiringlicenses")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'COMPLIANCE_LEAD', 'PRODUCT_OWNER')")
    public ResponseEntity<List<LicenseAlertDTO>> getExpiringLicenses(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getExpiringLicenses(days));
    }

    @GetMapping("/devicesatrisk")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPLIANCE_OFFICER', 'COMPLIANCE_LEAD', 'PRODUCT_OWNER')")
    public ResponseEntity<Long> getDevicesAtRisk(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getDevicesAtRisk(days));
    }
}
