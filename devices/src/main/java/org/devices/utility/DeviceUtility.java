package org.devices.utility;

import org.devices.dto.DeviceRequestDTO;
import org.devices.dto.DeviceResponseDTO;
import org.devices.entity.Device;
import org.devices.entity.DeviceStatus;

public class DeviceUtility {
    public static Device toEntity(DeviceRequestDTO dto) {
        Device d = new Device();
        d.setDeviceId(dto.getDeviceId());
        d.setIpAddress(dto.getIpAddress());
        d.setDeviceType(dto.getDeviceType());
        d.setLocation(dto.getLocation());
        d.setModel(dto.getModel());
        if (dto.getStatus() != null) {
            try {
                d.setStatus(DeviceStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                // keep default
            }
        }
        return d;
    }

        public static DeviceResponseDTO toDto(Device d) {
            DeviceResponseDTO dto = new DeviceResponseDTO();
            dto.setDeviceId(d.getDeviceId());
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
        // Only change allowed fields
        if (dto.getIpAddress() != null) d.setIpAddress(dto.getIpAddress());
        if (dto.getDeviceType() != null) d.setDeviceType(dto.getDeviceType());
        if (dto.getLocation() != null) d.setLocation(dto.getLocation());
        if (dto.getModel() != null) d.setModel(dto.getModel());
        if (dto.getStatus() != null) {
            try {
                d.setStatus(DeviceStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
