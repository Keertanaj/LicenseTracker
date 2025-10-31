package org.licensetracker.dto;

import lombok.*;
import org.licensetracker.entity.DeviceStatus;

import java.time.LocalDateTime;

@Getter
@Setter
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
