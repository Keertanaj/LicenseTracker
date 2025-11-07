package org.licensetracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.licensetracker.dto.*;
import org.licensetracker.entity.*;
import org.licensetracker.exception.*;
import org.licensetracker.repository.*;
import org.licensetracker.utility.DeviceUtility;
import org.licensetracker.utility.SoftwareUtility;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @InjectMocks
    private DeviceServiceImpl deviceService;

    // Mock injected dependencies
    @Mock
    private DeviceRepo deviceRepo;
    @Mock
    private AuditLogService auditLogService;
    @Mock
    private InstallationRepository installationRepo;
    @Mock
    private SoftwareRepository softwareRepo;
    // ObjectMapper is mocked but not used in the logic being tested
    @Mock
    private ObjectMapper objectMapper;

    // Mock Data Setup
    private Device mockDevice;
    private Device mockDeviceDecommissioned;
    private DeviceRequestDTO mockDeviceRequest;
    private DeviceResponseDTO mockDeviceResponse;
    private final String TEST_DEVICE_ID = "RTR-TEST-01";
    private final String TEST_IP = "10.0.0.1";
    private final Long TEST_USER_ID = 101L;

    @BeforeEach
    void setUp() {
        mockDevice = Device.builder()
                .deviceId(TEST_DEVICE_ID)
                .ipAddress(TEST_IP)
                .status(DeviceStatus.ACTIVE)
                .build();

        mockDeviceDecommissioned = Device.builder()
                .deviceId(TEST_DEVICE_ID)
                .ipAddress(TEST_IP)
                .status(DeviceStatus.DECOMMISSIONED)
                .build();

        mockDeviceRequest = DeviceRequestDTO.builder()
                .deviceId(TEST_DEVICE_ID)
                .ipAddress(TEST_IP)
                .status(DeviceStatus.ACTIVE)
                .build();

        mockDeviceResponse = DeviceResponseDTO.builder()
                .deviceId(TEST_DEVICE_ID)
                .build();

        // Mock Audit User Info (used by multiple tests)
        when(auditLogService.getCurrentUserInfo()).thenReturn(Map.of("userId", TEST_USER_ID));
    }

    // ----------------------------------------------------
    // TEST: addDevice
    // ----------------------------------------------------
    @Test
    void addDevice_Success() {
        try (MockedStatic<DeviceUtility> mockedStatic = mockStatic(DeviceUtility.class)) {
            // Stub static utility methods
            mockedStatic.when(() -> DeviceUtility.toEntity(any(DeviceRequestDTO.class))).thenReturn(mockDevice);
            mockedStatic.when(() -> DeviceUtility.toDto(any(Device.class))).thenReturn(mockDeviceResponse);

            when(deviceRepo.save(any(Device.class))).thenReturn(mockDevice);

            DeviceResponseDTO result = deviceService.addDevice(mockDeviceRequest);

            assertNotNull(result);
            verify(deviceRepo, times(1)).save(any(Device.class));
            verify(auditLogService, times(1)).createLog(any(AuditLogRequestDTO.class));
        }
    }

    // ----------------------------------------------------
    // TEST: updateDevice
    // ----------------------------------------------------
    @Test
    void updateDevice_Success() {
        DeviceRequestDTO updateRequest = new DeviceRequestDTO();
        updateRequest.setIpAddress("10.0.0.2");

        try (MockedStatic<DeviceUtility> mockedStatic = mockStatic(DeviceUtility.class)) {
            mockedStatic.when(() -> DeviceUtility.toDto(any(Device.class))).thenReturn(mockDeviceResponse);

            when(deviceRepo.findById(TEST_DEVICE_ID)).thenReturn(Optional.of(mockDevice));
            when(deviceRepo.findByIpAddress("10.0.0.2")).thenReturn(Optional.empty());
            when(deviceRepo.save(any(Device.class))).thenReturn(mockDevice);

            DeviceResponseDTO result = deviceService.updateDevice(TEST_DEVICE_ID, updateRequest);

            assertNotNull(result);
            verify(deviceRepo, times(1)).save(mockDevice);
        }
    }

    @Test
    void updateDevice_DuplicateIP_Failure() {
        Device conflictingDevice = Device.builder().deviceId("RTR-002").build();
        mockDeviceRequest.setDeviceId(TEST_DEVICE_ID); // Ensure request matches ID for this test

        when(deviceRepo.findById(TEST_DEVICE_ID)).thenReturn(Optional.of(mockDevice));
        // Mock finding a device with the same IP but a different ID
        when(deviceRepo.findByIpAddress(mockDeviceRequest.getIpAddress()))
                .thenReturn(Optional.of(conflictingDevice));

        assertThrows(DuplicateResourceException.class, () ->
                deviceService.updateDevice(TEST_DEVICE_ID, mockDeviceRequest));
    }

    // ----------------------------------------------------
    // TEST: deleteDevice
    // ----------------------------------------------------
    @Test
    void deleteDevice_Success() {
        when(deviceRepo.findById(TEST_DEVICE_ID)).thenReturn(Optional.of(mockDeviceDecommissioned));

        String result = deviceService.deleteDevice(TEST_DEVICE_ID);

        assertEquals("Device deleted successfully", result);
        verify(deviceRepo, times(1)).delete(mockDeviceDecommissioned);
        verify(auditLogService, times(1)).createLog(any(AuditLogRequestDTO.class));
    }

    @Test
    void deleteDevice_NotDecommissioned_Failure() {
        when(deviceRepo.findById(TEST_DEVICE_ID)).thenReturn(Optional.of(mockDevice)); // Status is ACTIVE

        assertThrows(BadRequestException.class, () ->
                deviceService.deleteDevice(TEST_DEVICE_ID));
        verify(deviceRepo, never()).delete(any(Device.class));
    }

    // ----------------------------------------------------
    // TEST: getSoftwareDetailsByDeviceId
    // ----------------------------------------------------
    @Test
    void getSoftwareDetails_Success_Outdated() {
        Software outdatedSoftware = Software.builder().softwareName("PAN-OS").currentVersion("9.1").latestVersion("10.0").build();
        Installation mockInstallation = Installation.builder().software(outdatedSoftware).build();
        SoftwareResponseDTO mockSoftwareResponse = SoftwareResponseDTO.builder().softwareName("PAN-OS").build();

        try (MockedStatic<SoftwareUtility> mockedStatic = mockStatic(SoftwareUtility.class)) {
            mockedStatic.when(() -> SoftwareUtility.toDto(any(Software.class))).thenReturn(mockSoftwareResponse);

            when(deviceRepo.findById(TEST_DEVICE_ID)).thenReturn(Optional.of(mockDevice));
            when(installationRepo.findByDevice_DeviceId(TEST_DEVICE_ID)).thenReturn(mockInstallation);

            SoftwareResponseDTO result = deviceService.getSoftwareDetailsByDeviceId(TEST_DEVICE_ID);

            assertNotNull(result);
            assertEquals("PAN-OS", result.getSoftwareName());
            // We rely on the utility to correctly map status, but the retrieval flow is tested.
        }
    }

    @Test
    void getSoftwareDetails_NotInstalled_Failure() {
        when(deviceRepo.findById(TEST_DEVICE_ID)).thenReturn(Optional.of(mockDevice));
        when(installationRepo.findByDevice_DeviceId(TEST_DEVICE_ID)).thenReturn(null);

        assertThrows(SoftwareNotInstalledException.class, () ->
                deviceService.getSoftwareDetailsByDeviceId(TEST_DEVICE_ID));
    }

    // ----------------------------------------------------
    // TEST: renewSoftwareVersion
    // ----------------------------------------------------
    @Test
    void renewSoftwareVersion_Success() {
        Software outdatedSoftware = Software.builder().currentVersion("1.0").latestVersion("2.0").build();
        Installation mockInstallation = Installation.builder().software(outdatedSoftware).build();

        when(installationRepo.findByDevice_DeviceId(TEST_DEVICE_ID)).thenReturn(mockInstallation);

        deviceService.renewSoftwareVersion(TEST_DEVICE_ID);

        // Verify version update happened
        assertEquals("2.0", outdatedSoftware.getCurrentVersion());
        verify(softwareRepo, times(1)).save(outdatedSoftware);
    }

    @Test
    void renewSoftwareVersion_AlreadyCurrent_Failure() {
        Software currentSoftware = Software.builder().currentVersion("2.0").latestVersion("2.0").build();
        Installation mockInstallation = Installation.builder().software(currentSoftware).build();

        when(installationRepo.findByDevice_DeviceId(TEST_DEVICE_ID)).thenReturn(mockInstallation);

        assertThrows(SoftwareAlreadyCurrentException.class, () ->
                deviceService.renewSoftwareVersion(TEST_DEVICE_ID));

        verify(softwareRepo, never()).save(any());
    }
}