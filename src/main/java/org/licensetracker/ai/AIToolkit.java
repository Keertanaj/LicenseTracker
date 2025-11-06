package org.licensetracker.ai;

import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.service.DeviceService;
import org.licensetracker.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class AIToolkit {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private DeviceService deviceService;

    public List<LicenseResponseDTO> listAllLicenses() {
        return licenseService.listLicenses();
    }
    public List<LicenseAlertDTO> findExpiringLicenses(int days) {
        return licenseService.getExpiringLicenses(days);
    }
    public List<String> getSoftwareNames() {
        return licenseService.getAllSoftware();
    }
    public List<DeviceResponseDTO> listAllDevices() {
        return deviceService.listDevices();
    }
    public List<String> getAllDeviceLocations() {
        return deviceService.getAllLocations();
    }
}