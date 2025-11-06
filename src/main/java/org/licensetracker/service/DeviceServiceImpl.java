package org.licensetracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.licensetracker.dto.*;
import org.licensetracker.entity.Device;
import org.licensetracker.entity.DeviceStatus;
import org.licensetracker.entity.Installation;
import org.licensetracker.entity.Software;
import org.licensetracker.exception.*;
import org.licensetracker.repository.DeviceRepo;
import org.licensetracker.repository.InstallationRepository;
import org.licensetracker.repository.SoftwareRepository;
import org.licensetracker.utility.DeviceUtility;
import org.licensetracker.utility.SoftwareUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    public DeviceRepo deviceRepo;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private InstallationRepository installationRepo;

    @Autowired
    private SoftwareRepository softwareRepo;

    @Autowired
    private ObjectMapper objectMapper;
    private Map<String, Object> getUserInfoForAudit() {
        return auditLogService.getCurrentUserInfo();
    }

    @Override
    public DeviceResponseDTO addDevice(DeviceRequestDTO request) {
        Device device = DeviceUtility.toEntity(request);
        Device saved = deviceRepo.save(device);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");
            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("DEVICE")
                    .entityId("1")
                    .action("CREATE")
                    .details("Created a device")
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for device creation: " + e.getMessage());
        }
        return DeviceUtility.toDto(saved);
    }

    @Override
    public DeviceResponseDTO updateDevice(String deviceId, DeviceRequestDTO request) {
        Device oldDevice = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        deviceRepo.findByIpAddress(request.getIpAddress())
                .ifPresent(d -> {
                    if (!d.getDeviceId().equals(deviceId)) {
                        throw new DuplicateResourceException(
                                "IP address already in use: " + request.getIpAddress());
                    }
                });
        DeviceUtility.updateEntityFromDto(oldDevice, request);
        Device saved = deviceRepo.save(oldDevice);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("DEVICE")
                    .entityId("1")
                    .action("UPDATE")
                    .details("Updated a device")
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for device update: " + e.getMessage());
        }
        return DeviceUtility.toDto(saved);
    }

    @Override
    public String deleteDevice(String deviceId) {
        Device existing = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        if (existing.getStatus() != null && existing.getStatus() != DeviceStatus.DECOMMISSIONED) {
            throw new BadRequestException("Device must be DECOMMISSIONED before deletion");
        }
        deviceRepo.delete(existing);

        try {
            Map<String, Object> userInfo = getUserInfoForAudit();
            Long currentUserId = (Long) userInfo.get("userId");

            AuditLogRequestDTO logRequest = AuditLogRequestDTO.builder()
                    .userId(currentUserId)
                    .entityType("DEVICE")
                    .entityId("1")
                    .action("DELETE")
                    .details("Deleted a device")
                    .build();
            auditLogService.createLog(logRequest);

        } catch (Exception e) {
            System.err.println("Audit logging failed for device deletion: " + e.getMessage());
        }
        return "Device deleted successfully";
    }

    @Override
    public List<DeviceResponseDTO> listDevices() {
        return deviceRepo.findAll().stream()
                .map(DeviceUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceResponseDTO> searchDevices(String ipAddress, String location) {
        List<Device> devices;
        if (ipAddress != null && !ipAddress.isEmpty() && location != null && !location.isEmpty()) {
            devices = deviceRepo.findByIpAddressContainingIgnoreCaseAndLocationIgnoreCase(ipAddress, location);
        } else if (ipAddress != null && !ipAddress.isEmpty()) {
            devices = deviceRepo.findByIpAddressContainingIgnoreCase(ipAddress);
        } else if (location != null && !location.isEmpty()) {
            devices = deviceRepo.findByLocationIgnoreCase(location);
        } else {
            devices = deviceRepo.findAll();
        }
        return devices.stream().map(DeviceUtility::toDto).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllLocations() {
        return deviceRepo.findDistinctLocations();
    }

    @Override
    public List<String> getAllIpAddresses() {
        return deviceRepo.findDistinctIpAddresses();
    }

    @Override
    public Device getDeviceById(String deviceId) {
        return deviceRepo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + deviceId));
    }

    @Override
    public SoftwareResponseDTO getSoftwareDetailsByDeviceId(String deviceId) {
        Device device = getDeviceById(deviceId);
        Installation installation = installationRepo.findByDevice_DeviceId(deviceId);
        if (installation == null) {
            throw new SoftwareNotInstalledException("No software installation found for device: " + deviceId);
        }
        Software software = installation.getSoftware();
        boolean outdated = !software.getCurrentVersion().equals(software.getLatestVersion());
        SoftwareResponseDTO response = SoftwareUtility.toDto(software);
        return response;
    }

    @Override
    public void renewSoftwareVersion(String deviceId) {
        Installation installation = installationRepo.findByDevice_DeviceId(deviceId);
        if (installation == null) {
            throw new SoftwareNotInstalledException("No software installation found for device: " + deviceId);
        }

        Software software = installation.getSoftware();

        if (!software.getCurrentVersion().equals(software.getLatestVersion())) {
            software.setCurrentVersion(software.getLatestVersion());
            softwareRepo.save(software);

        } else {
            throw new SoftwareAlreadyCurrentException("Software version is already current.");
        }
    }
}