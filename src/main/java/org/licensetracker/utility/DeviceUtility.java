package org.licensetracker.utility;

import org.licensetracker.dto.DeviceRequestDTO;
import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.entity.Device;

public class DeviceUtility {
    public static Device toEntity(DeviceRequestDTO dto) {
        Device d = new Device();
        d.setDeviceId(dto.getDeviceId());
        d.setDeviceName(dto.getDeviceName()); // Corrected this line
        d.setIpAddress(dto.getIpAddress());
        d.setDeviceType(dto.getDeviceType());
        d.setLocation(dto.getLocation());
        d.setModel(dto.getModel());
        if (dto.getStatus() != null) {
            try {
                d.setStatus(dto.getStatus());
            } catch (IllegalArgumentException ex) {
            }
        }
        return d;
    }

    public static DeviceResponseDTO toDto(Device d) {
        DeviceResponseDTO dto = new DeviceResponseDTO();
        dto.setDeviceId(d.getDeviceId());
        dto.setDeviceName(d.getDeviceName());
        dto.setIpAddress(d.getIpAddress());
        dto.setDeviceType(d.getDeviceType());
        dto.setLocation(d.getLocation());
        dto.setModel(d.getModel());
        dto.setStatus(d.getStatus() == null ? null : d.getStatus().name());
        dto.setCreatedAt(d.getCreatedAt());
        dto.setUpdatedAt(d.getUpdatedAt());
        return dto;
    }

    public static void updateEntityFromDto(Device d, DeviceRequestDTO dto) {
        if (dto.getDeviceName() != null) d.setDeviceName(dto.getDeviceName());
        if (dto.getIpAddress() != null) d.setIpAddress(dto.getIpAddress());
        if (dto.getDeviceType() != null) d.setDeviceType(dto.getDeviceType());
        if (dto.getLocation() != null) d.setLocation(dto.getLocation());
        if (dto.getModel() != null) d.setModel(dto.getModel());
        if (dto.getStatus() != null) {
            try {
                d.setStatus(dto.getStatus());
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
