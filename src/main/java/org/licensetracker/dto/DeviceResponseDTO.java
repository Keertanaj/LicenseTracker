package org.licensetracker.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceResponseDTO {
    private String deviceId;
    private String deviceName;
    private String ipAddress;
    private String deviceType;
    private String location;
    private String model;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
