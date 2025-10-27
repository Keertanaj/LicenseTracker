package org.licensetracker.service;

import org.licensetracker.dto.DeviceRequestDTO;
import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.entity.Device;
import org.licensetracker.entity.DeviceStatus;
import org.licensetracker.exception.BadRequestException;
import org.licensetracker.exception.DuplicateResourceException;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.DeviceRepo;
import org.licensetracker.utility.DeviceUtility;
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
    @Override
    public List<DeviceResponseDTO> searchDevices(String ipAddress, String location) {
        List<Device> devices;

        if (ipAddress != null && !ipAddress.isEmpty()) {
            devices = deviceRepo.findByIpAddressContainingIgnoreCase(ipAddress);
        } else if (location != null && !location.isEmpty()) {
            devices = deviceRepo.findByLocationIgnoreCase(location);
        } else {
            devices = deviceRepo.findAll();
        }

        return devices.stream().map(DeviceUtility::toDto).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllLocations() {
        return deviceRepo.findDistinctLocations();
    }
    @Override
    public List<String> getAllIpAddresses() {
        return deviceRepo.findDistinctIpAddresses();
    }
}
