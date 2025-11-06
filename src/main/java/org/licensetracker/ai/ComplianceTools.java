package org.licensetracker.ai;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.service.AssignmentService;
import org.licensetracker.service.DeviceService;
import org.licensetracker.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComplianceTools {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AssignmentService assignmentService;

    @Tool("Retrieves licenses expiring within a specified number of days (e.g., 30, 90, 180).")
    public List<LicenseAlertDTO> findExpiringLicenses(@P("The number of days from today to check for expiring licenses.") int days) {
        return licenseService.getExpiringLicenses(days);
    }

    @Tool("Finds licenses matching a specific vendor name or software name. Leave parameters empty to get all licenses.")
    public List<LicenseAlertDTO> searchLicenses(
            @P("The name of the vendor. Can be a partial name.") String vendorName,
            @P("The name of the software. Can be a partial name.") String softwareName) {
        List<LicenseResponseDTO> licenses = licenseService.searchLicenses(vendorName, softwareName);
        return licenses.stream()
                .map(this::convertToLicenseAlertDTO)
                .collect(Collectors.toList());
    }

    private LicenseAlertDTO convertToLicenseAlertDTO(LicenseResponseDTO license) {
        return LicenseAlertDTO.builder()
                .licenseKey(license.getLicenseKey())
                .softwareName(license.getSoftwareName())
                .vendor(license.getVendorName())
                .validTo(license.getValidTo())
                .devicesUsed(assignmentService.getCurrentLicenseUsage(license.getLicenseKey()))
                .build();
    }


    @Tool("Retrieves the full inventory of all network devices.")
    public List<DeviceResponseDTO> listAllDevices() {
        return deviceService.listDevices();
    }

    @Tool("Searches devices based on a partial IP address or location name.")
    public List<DeviceResponseDTO> searchDevices(
            @P("A partial or full IP address to search for.") String ipAddressQuery,
            @P("The location of the device to search for (e.g., 'BLR', 'NYC').") String locationQuery) {
        return deviceService.searchDevices(ipAddressQuery, locationQuery);
    }

    @Tool("Retrieves a list of all distinct network locations where devices are deployed.")
    public List<String> getAllDeviceLocations() {
        return deviceService.getAllLocations();
    }
}
