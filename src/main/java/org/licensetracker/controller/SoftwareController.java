package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.SoftwareRequestDTO;
import org.licensetracker.dto.SoftwareResponseDTO;
import org.licensetracker.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/software")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoftwareResponseDTO> addSoftware(@RequestBody SoftwareRequestDTO request) {
        return ResponseEntity.ok(softwareService.addSoftware(request));
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllSoftwareNames() {
        return ResponseEntity.ok(softwareService.getAllSoftwareNames());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SoftwareResponseDTO>> getAllSoftware() {
        return ResponseEntity.ok(softwareService.getAllSoftware());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoftwareResponseDTO> updateSoftware(@PathVariable Integer id, @RequestBody SoftwareRequestDTO request) {
        return ResponseEntity.ok(softwareService.updateSoftware(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSoftware(@PathVariable Integer id) {
        softwareService.deleteSoftware(id);
        return ResponseEntity.noContent().build();
    }
}
