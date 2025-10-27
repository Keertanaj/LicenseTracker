package org.licensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.licensetracker.entity.DeviceStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceRequestDTO {
    private String deviceId;
    private String deviceName;
    private String ipAddress;
    private String deviceType;
    private String location;
    private String model;
    private DeviceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
