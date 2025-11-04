package org.licensetracker.service;

import org.licensetracker.dto.SoftwareRequestDTO;
import org.licensetracker.dto.SoftwareResponseDTO;

import java.util.List;

public interface SoftwareService {
    SoftwareResponseDTO addSoftware(SoftwareRequestDTO request);
    List<String> getAllSoftwareNames();

    SoftwareResponseDTO updateSoftware(Integer id, SoftwareRequestDTO request);
    void deleteSoftware(Integer id);
    List<SoftwareResponseDTO> getAllSoftware();
}
