package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.AssignmentRequestDTO;
import org.licensetracker.dto.AssignmentResponseDTO;
import org.licensetracker.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
@SecurityRequirement(name = "Bearer Authentication")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/assignlicense")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public ResponseEntity<AssignmentResponseDTO> assignLicense(@RequestBody AssignmentRequestDTO assignmentRequestDto) {
        AssignmentResponseDTO newAssignmentDto = assignmentService.assignLicense(assignmentRequestDto);
        return new ResponseEntity<>(newAssignmentDto, HttpStatus.CREATED);
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public List<AssignmentResponseDTO> getAssignmentsByDeviceId(@PathVariable String deviceId) {
        return assignmentService.getAssignmentsByDeviceId(deviceId);
    }

    @GetMapping("/license/{licenseKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public List<AssignmentResponseDTO> getAssignmentsByLicenseKey(@PathVariable String licenseKey) {
        return assignmentService.getAssignmentsByLicenseKey(licenseKey);
    }

    @GetMapping("/usage/{licenseKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public Long getLicenseUsage(@PathVariable String licenseKey) {
        return assignmentService.getCurrentLicenseUsage(licenseKey);
    }

    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public ResponseEntity<Void> unassignLicense(@PathVariable Integer assignmentId) {
        assignmentService.unassignLicense(assignmentId);
        return ResponseEntity.noContent().build();
    }
}
