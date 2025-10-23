package org.devices.service;

import org.devices.dto.DeviceRequestDTO;
import org.devices.dto.DeviceResponseDTO;
import org.devices.entity.Device;
import org.devices.entity.DeviceStatus;
import org.devices.exception.BadRequestException;
import org.devices.exception.DuplicateResourceException;
import org.devices.exception.ResourceNotFoundException;
import org.devices.repository.DeviceRepo;
import org.devices.utility.DeviceUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService{
    @Autowired
    public DeviceRepo deviceRepo;
    @Override
    public DeviceResponseDTO addDevice(DeviceRequestDTO request) {
        Device device = DeviceUtility.toEntity(request);
        Device saved = deviceRepo.save(device);
        return DeviceUtility.toDto(saved);
    }

    @Override
    public List<DeviceResponseDTO> listDevices(){
        return deviceRepo.findAll().stream()
                .map(DeviceUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceResponseDTO updateDevice(String deviceId, DeviceRequestDTO request) {
        Device device= deviceRepo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        deviceRepo.findByIpAddress(request.getIpAddress())
                .ifPresent(d -> {
                    if (!d.getDeviceId().equals(deviceId)) {
                        throw new DuplicateResourceException(
                                "IP address already in use: " + request.getIpAddress());
                    }
                });
        DeviceUtility.updateEntityFromDto(device, request);
        Device saved = deviceRepo.save(device);
        return DeviceUtility.toDto(saved);
    }

    @Override
    public String deleteDevice(String deviceId) {
        Device existing = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        if (existing.getStatus() != null && existing.getStatus() != DeviceStatus.DECOMMISSIONED) {
            throw new BadRequestException("Device must be DECOMMISSIONED before deletion");
        }
        deviceRepo.delete(existing);
        return "Device deleted successfully";
    }
}
