package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/software")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;
/*
    @PostMapping
    public ResponseEntity<SoftwareResponseDTO> addSoftware(@RequestBody SoftwareRequestDTO request) {
        return ResponseEntity.ok(softwareService.addSoftware(request));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<SoftwareResponseDTO>> getSoftwareByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(softwareService.getSoftwareByDeviceId(deviceId));
    }

 */

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllSoftwareNames() {
        return ResponseEntity.ok(softwareService.getAllSoftwareNames());
    }
}
