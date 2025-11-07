package org.licensetracker.utility;

import org.licensetracker.dto.AssignmentRequestDTO;
import org.licensetracker.dto.AssignmentResponseDTO;
import org.licensetracker.entity.Assignment;
import org.licensetracker.entity.Device;
import org.licensetracker.entity.License;
import org.licensetracker.repository.DeviceRepo;
import org.licensetracker.repository.LicenseRepository;

import java.time.LocalDate;
import java.util.Optional;

public class AssignmentUtility {

    public static Assignment toEntity(AssignmentRequestDTO dto, DeviceRepo deviceRepository, LicenseRepository licenseRepository) {
        Assignment a = new Assignment();

        Optional<Device> deviceOpt = deviceRepository.findById(dto.getDeviceId());
        Optional<License> licenseOpt = licenseRepository.findById(dto.getLicenseId());

        Device device = deviceOpt.orElseThrow(() -> new IllegalArgumentException("Device not found with ID: " + dto.getDeviceId()));
        License license = licenseOpt.orElseThrow(() -> new IllegalArgumentException("License not found with ID: " + dto.getLicenseId()));

        a.setDevice(device);
        a.setLicense(license);
        a.setAssignedOn(LocalDate.now());
        return a;
    }

    public static AssignmentResponseDTO toDto(Assignment a) {
        AssignmentResponseDTO dto = new AssignmentResponseDTO();
        dto.setAssignmentId(a.getAssignmentId());
        dto.setDeviceId(a.getDevice() != null ? a.getDevice().getDeviceId() : null);
        dto.setLicenseId(a.getLicense() != null ? a.getLicense().getLicenseKey() : null);
        // Populate the new softwareName field
        if (a.getLicense() != null) {
            dto.setSoftwareName(a.getLicense().getSoftwareName());
        }
        dto.setAssignmentDate(a.getAssignedOn());
        return dto;
    }
}
