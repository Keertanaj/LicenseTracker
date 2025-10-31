package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.ReportDTO;
import org.licensetracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/licenses")
    public ResponseEntity<List<ReportDTO>> getLicenseReport(@RequestParam(required = false) String vendor, @RequestParam(required = false) String software,@RequestParam(required = false) String location) {
        List<ReportDTO> data = reportService.generateReport(vendor, software, location);
        return ResponseEntity.ok(data);
    }
}
