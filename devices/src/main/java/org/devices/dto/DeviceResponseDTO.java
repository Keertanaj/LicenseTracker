package org.devices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceResponseDTO {
    private String deviceId;
    private String ipAddress;
    private String DeviceType;
    private String location;
    private String model;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
