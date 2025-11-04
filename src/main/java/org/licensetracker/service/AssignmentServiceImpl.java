package org.licensetracker.service;

import org.licensetracker.dto.AssignmentRequestDTO;
import org.licensetracker.dto.AssignmentResponseDTO;
import org.licensetracker.entity.Assignment;
import org.licensetracker.entity.License;
import org.licensetracker.entity.Software;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.AssignmentRepository;
import org.licensetracker.repository.DeviceRepo;
import org.licensetracker.repository.LicenseRepository;
import org.licensetracker.repository.SoftwareRepository;
import org.licensetracker.utility.AssignmentUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private DeviceRepo deviceRepository;
    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private SoftwareRepository softwareRepository; // Injected to find software

    @Autowired
    private InstallationService installationService; // Injected to create installation

    @Transactional
    public AssignmentResponseDTO assignLicense(AssignmentRequestDTO assignmentRequestDto) {
        String deviceId = assignmentRequestDto.getDeviceId();
        String licenseKey = assignmentRequestDto.getLicenseId();
        Assignment assignment = AssignmentUtility.toEntity(
                assignmentRequestDto,
                deviceRepository,
                licenseRepository
        );
        License license = assignment.getLicense();
        Long currentUsage = assignmentRepository.countAssignmentsByLicenseKey(licenseKey);

        if (license.getMaxUsage() != null && currentUsage >= license.getMaxUsage()) {
            throw new IllegalStateException(
                    "Max Usage Exceeded. This license is currently at full capacity (" + license.getMaxUsage() + ")."
            );
        }
        boolean alreadyAssigned = assignmentRepository.findByDevice_DeviceId(deviceId).stream()
                .anyMatch(a -> a.getLicense().getLicenseKey().equals(licenseKey));

        if (alreadyAssigned) {
            throw new IllegalArgumentException(
                    "License '" + licenseKey + "' is already assigned to device '" + deviceId + "'."
            );
        }
        Assignment savedAssignment = assignmentRepository.save(assignment);
        String softwareName = savedAssignment.getLicense().getSoftwareName();
        Software software = softwareRepository.findBySoftwareName(softwareName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot create installation. Software named '" + softwareName + "' does not exist in the master software list."
                ));
        installationService.createInstallation(savedAssignment.getDevice(), software);
        return AssignmentUtility.toDto(savedAssignment);
    }

    public List<AssignmentResponseDTO> getAssignmentsByDeviceId(String deviceId) {
        return assignmentRepository.findByDevice_DeviceId(deviceId).stream()
                .map(AssignmentUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentResponseDTO> getAssignmentsByLicenseKey(String licenseKey) {
        return assignmentRepository.findByLicense_LicenseKey(licenseKey).stream()
                .map(AssignmentUtility::toDto)
                .collect(Collectors.toList());
    }

    public Long getCurrentLicenseUsage(String licenseKey) {
        return assignmentRepository.countAssignmentsByLicenseKey(licenseKey);
    }

    public void unassignLicense(Integer assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignmentId));

        assignmentRepository.delete(assignment);
    }
}
