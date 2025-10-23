package org.devices.controller;

import org.devices.dto.DeviceRequestDTO;
import org.devices.dto.DeviceResponseDTO;
import org.devices.entity.DeviceStatus;
import org.devices.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
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
        request.setStatus(status.name());
        DeviceResponseDTO updated = deviceService.updateDevice(deviceId, request);
        return ResponseEntity.ok(updated);
    }
}
