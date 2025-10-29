package org.licensetracker.repository;

import org.licensetracker.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepo extends JpaRepository<Device, String> {
    Optional<Device> findByIpAddress(String ipAddress);
    boolean existsByDeviceId(String deviceId);
    boolean existsByIpAddress(String ipAddress);
    List<Device> findByIpAddressContainingIgnoreCase(String ipAddress);
    List<Device> findByLocationIgnoreCase(String location);
    @Query("SELECT DISTINCT d.location FROM Device d")
    List<String> findDistinctLocations();
    @Query("SELECT DISTINCT d.ipAddress FROM Device d")
    List<String> findDistinctIpAddresses();
}