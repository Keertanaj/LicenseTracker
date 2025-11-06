package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.dto.DeviceRequestDTO;
import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.entity.Device;
import org.licensetracker.entity.DeviceStatus;
import org.licensetracker.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<DeviceResponseDTO> addDevice(@RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO response = deviceService.addDevice(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> listDevices() {
        List<DeviceResponseDTO> devices = deviceService.listDevices();
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @PathVariable String deviceId,
            @RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO updated = deviceService.updateDevice(deviceId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<String> deleteDevice(@PathVariable String deviceId) {
        String message = deviceService.deleteDevice(deviceId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{deviceId}/status")
    public ResponseEntity<DeviceResponseDTO> updateStatus(
            @PathVariable String deviceId,
            @RequestParam DeviceStatus status) {
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setStatus(status);
        DeviceResponseDTO updated = deviceService.updateDevice(deviceId, request);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/search")
    public ResponseEntity<List<DeviceResponseDTO>> searchDevices(@RequestParam(required = false) String ipAddress,  @RequestParam(required = false) String location) {
        List<DeviceResponseDTO> devices = deviceService.searchDevices(ipAddress, location);
        return ResponseEntity.ok(devices);
    }
    @GetMapping("/locations")
    public ResponseEntity<List<String>> getAllLocations() {
        List<String> locations = deviceService.getAllLocations();
        return ResponseEntity.ok(locations);
    }
    @GetMapping("/ipaddresses")
    public ResponseEntity<List<String>> getAllIpAddresses() {
        List<String> ipaddresses = deviceService.getAllIpAddresses();
        return ResponseEntity.ok(ipaddresses);
    }
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String deviceId) {
        Device device = deviceService.getDeviceById(deviceId);
        return ResponseEntity.ok().body(device);
    }
}
