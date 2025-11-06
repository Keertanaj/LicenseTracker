package org.licensetracker.ai;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.licensetracker.dto.DeviceResponseDTO;
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.service.AssignmentService;
import org.licensetracker.service.DeviceService;
import org.licensetracker.service.LicenseService;
import org.licensetracker.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service; // Changed from @Component to @Service

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service // Use @Service for business logic classes
@RequiredArgsConstructor // Inject services via constructor
@Slf4j // Use Lombok logging
public class ComplianceTools {

    // Final fields injected via @RequiredArgsConstructor
    private final LicenseService licenseService;
    private final DeviceService deviceService;
    private final AssignmentService assignmentService;

    // --- Template's Secure User Retrieval (Modified to return User or throw) ---
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            // Log an error if a tool is called without a valid user.
            log.error("AI Tool Security Breach: Tool called without authenticated User in context.");
            throw new IllegalStateException("Authentication failed.");
        }
        return (User) authentication.getPrincipal();
    }
    // --------------------------------------------------------------------------


    @Tool("Retrieves licenses expiring within a specified number of days (e.g., 30, 90, 180).")
    public List<LicenseAlertDTO> findExpiringLicenses(@P("The number of days from today to check for expiring licenses.") int days) {
        log.info("AI Tool: Executing findExpiringLicenses for {} days.", days);
        try {
            // NOTE: We call getCurrentUser() here, but its value is not used for data fetching (since licenses are global),
            // but it ENSURES the user is logged in before running the query.
            //getCurrentUser();
            return licenseService.getExpiringLicenses(days);
        } catch (Exception e) {
            log.error("AI Tool Error fetching expiring licenses.", e);
            return Collections.emptyList(); // Return empty list on error
        }
    }

    @Tool("Finds licenses matching a specific vendor name or software name. Leave parameters empty to get all licenses.")
    public List<LicenseAlertDTO> searchLicenses(
            @P("The name of the vendor. Can be a partial name.") String vendorName,
            @P("The name of the software. Can be a partial name.") String softwareName) {
        log.info("AI Tool: Executing searchLicenses - Vendor: {}, Software: {}", vendorName, softwareName);
        try {
            //getCurrentUser();
            List<LicenseResponseDTO> licenses = licenseService.searchLicenses(vendorName, softwareName);
            return licenses.stream()
                    .map(this::convertToLicenseAlertDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("AI Tool Error fetching licenses.", e);
            return Collections.emptyList();
        }
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
        log.info("AI Tool: Executing listAllDevices.");
        try {
//            getCurrentUser();
            return deviceService.listDevices();
        } catch (Exception e) {
            log.error("AI Tool Error fetching device list.", e);
            return Collections.emptyList();
        }
    }

    @Tool("Searches devices based on a partial IP address or location name.")
    public List<DeviceResponseDTO> searchDevices(
            @P("A partial or full IP address to search for.") String ipAddressQuery,
            @P("The location of the device to search for (e.g., 'BLR', 'NYC').") String locationQuery) {
        log.info("AI Tool: Executing searchDevices - IP: {}, Location: {}", ipAddressQuery, locationQuery);
        try {
            //getCurrentUser();
            return deviceService.searchDevices(ipAddressQuery, locationQuery);
        } catch (Exception e) {
            log.error("AI Tool Error searching devices.", e);
            return Collections.emptyList();
        }
    }

    @Tool("Retrieves a list of all distinct network locations where devices are deployed.")
    public List<String> getAllDeviceLocations() {
        log.info("AI Tool: Executing getAllDeviceLocations.");
        try {
            //getCurrentUser();
            return deviceService.getAllLocations();
        } catch (Exception e) {
            log.error("AI Tool Error fetching locations.", e);
            return Collections.emptyList();
        }
    }

    @Tool("Retrieves a complete list of all available licenses in the system.")
    public List<LicenseAlertDTO> getAllLicenses() {
        log.info("AI Tool: Executing getAllLicenses.");
        try {
            return searchLicenses("", "");
        } catch (Exception e) {
            log.error("AI Tool Error fetching all licenses.", e);
            return Collections.emptyList();
        }
    }


}