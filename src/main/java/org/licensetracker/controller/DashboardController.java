package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.DashboardMetricsDTO;
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }

    @GetMapping("/expiringlicenses")
    public ResponseEntity<List<LicenseAlertDTO>> getExpiringLicenses(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getExpiringLicenses(days));
    }

    @GetMapping("/devicesatrisk")
    public ResponseEntity<Long> getDevicesAtRisk(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getDevicesAtRisk(days));
    }
}
