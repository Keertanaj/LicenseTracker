package org.devices.repository;

import org.devices.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepo extends JpaRepository<Device, String> {
    Optional<Device> findByIpAddress(String ipAddress);
    boolean existsByDeviceId(String deviceId);
    boolean existsByIpAddress(String ipAddress);
}