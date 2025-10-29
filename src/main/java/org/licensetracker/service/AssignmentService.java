package org.licensetracker.service;

import org.licensetracker.dto.AssignmentRequestDTO;
import org.licensetracker.dto.AssignmentResponseDTO;

import java.util.List;

public interface AssignmentService {
    public AssignmentResponseDTO assignLicense(AssignmentRequestDTO assignmentRequestDto);
    public List<AssignmentResponseDTO> getAssignmentsByDeviceId(String deviceId);
    public List<AssignmentResponseDTO> getAssignmentsByLicenseKey(String licenseKey);
    public Long getCurrentLicenseUsage(String licenseKey);
    public void unassignLicense(Integer assignmentId);
}
