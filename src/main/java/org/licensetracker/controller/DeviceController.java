package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.DeviceRequestDTO;
import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.dto.SoftwareResponseDTO;
import org.licensetracker.entity.Device;
import org.licensetracker.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@SecurityRequirement(name = "Bearer Authentication")

public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public ResponseEntity<DeviceResponseDTO> addDevice(@RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO response = deviceService.addDevice(request);
        return ResponseEntity.ok(response);
    }
    private static final String READ_DEVICE_ROLES =
            "hasAnyRole('ADMIN', 'SECURITY_HEAD', 'PRODUCT_OWNER', 'COMPLIANCE_LEAD', 'COMPLIANCE_OFFICER', 'PROCUREMENT_LEAD', 'PROCUREMENT_OFFICER', 'OPERATIONS_MANAGER', 'IT_AUDITOR', 'NETWORK_ADMIN', 'NETWORK_ENGINEER')";

    @GetMapping
    @PreAuthorize(READ_DEVICE_ROLES)
    public ResponseEntity<List<DeviceResponseDTO>> listDevices() {
        List<DeviceResponseDTO> devices = deviceService.listDevices();
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @PathVariable String deviceId,
            @RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO updated = deviceService.updateDevice(deviceId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATIONS_MANAGER')")
    public ResponseEntity<String> deleteDevice(@PathVariable String deviceId) {
        String message = deviceService.deleteDevice(deviceId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/search")
    @PreAuthorize(READ_DEVICE_ROLES)
    public ResponseEntity<List<DeviceResponseDTO>> searchDevices(@RequestParam(required = false) String ipAddress,  @RequestParam(required = false) String location) {
        List<DeviceResponseDTO> devices = deviceService.searchDevices(ipAddress, location);
        return ResponseEntity.ok(devices);
    }
    @GetMapping("/locations")
    @PreAuthorize(READ_DEVICE_ROLES)
    public ResponseEntity<List<String>> getAllLocations() {
        List<String> locations = deviceService.getAllLocations();
        return ResponseEntity.ok(locations);
    }
    @GetMapping("/ipaddresses")
    @PreAuthorize(READ_DEVICE_ROLES)
    public ResponseEntity<List<String>> getAllIpAddresses() {
        List<String> ipaddresses = deviceService.getAllIpAddresses();
        return ResponseEntity.ok(ipaddresses);
    }
    @GetMapping("/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public ResponseEntity<Device> getDeviceById(@PathVariable String deviceId) {
        Device device = deviceService.getDeviceById(deviceId);
        return ResponseEntity.ok().body(device);
    }

    @GetMapping("/{deviceId}/software-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ADMIN')")
    public ResponseEntity<SoftwareResponseDTO> getSoftwareStatus(@PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.getSoftwareDetailsByDeviceId(deviceId));
    }

    // PUT: Renew/Update Software Version (Use Case 9)
    @PutMapping("/{deviceId}/renew-version")
    @PreAuthorize("hasAnyRole('ADMIN', 'NETWORK_ENGINEER', 'NETWORK_ADMIN')")
    public ResponseEntity<Void> renewSoftware(@PathVariable String deviceId) {
        deviceService.renewSoftwareVersion(deviceId);
        return ResponseEntity.noContent().build();
    }
}
