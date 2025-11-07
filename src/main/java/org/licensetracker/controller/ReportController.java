package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.ReportDTO;
import org.licensetracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@SecurityRequirement(name = "Bearer Authentication")

public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/licenses")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_OFFICER', 'IT_AUDITOR', 'COMPLIANCE_OFFICER', 'SECURITY_HEAD', 'COMPLIANCE_LEAD', 'PROCUREMENT_LEAD')")
    public ResponseEntity<List<ReportDTO>> getLicenseReport(@RequestParam(required = false) String vendor, @RequestParam(required = false) String software,@RequestParam(required = false) String location) {
        List<ReportDTO> data = reportService.generateReport(vendor, software, location);
        return ResponseEntity.ok(data);
    }
}
