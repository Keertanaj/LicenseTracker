package org.licensetracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Builder
@Entity
@Table(name = "device")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private String deviceId;
    private String deviceName;
    private String ipAddress;
    private String deviceType;
    private String location;
    private String model;
    @Enumerated(EnumType.STRING)
    private DeviceStatus status = DeviceStatus.ACTIVE;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
