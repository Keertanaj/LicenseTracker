package org.licensetracker.service;

import org.licensetracker.dto.DeviceRequestDTO;
import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.dto.SoftwareRequestDTO;
import org.licensetracker.dto.SoftwareResponseDTO;
import org.licensetracker.entity.Device;

import java.util.List;

public interface DeviceService {
    public  DeviceResponseDTO addDevice(DeviceRequestDTO deviceDTO);
    public List<DeviceResponseDTO> listDevices();
    public DeviceResponseDTO updateDevice(String deviceId, DeviceRequestDTO deviceDTO);
    public String deleteDevice(String deviceId);
    List<DeviceResponseDTO> searchDevices(String ipAddress, String location);
    List<String> getAllLocations();
    List<String> getAllIpAddresses();
    public Device getDeviceById(String deviceId);
    SoftwareResponseDTO getSoftwareDetailsByDeviceId(String deviceId);
    void renewSoftwareVersion(String deviceId);
}
