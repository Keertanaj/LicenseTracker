package org.devices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceRequestDTO {
    private String deviceId;
    private String ipAddress;
    private String DeviceType;
    private String location;
    private String model;
    private String status;
}
