package org.licensetracker.service;

import java.util.List;

public interface SoftwareService {
    /*
    SoftwareResponseDTO addSoftware(SoftwareRequestDTO request);
    List<SoftwareResponseDTO> getSoftwareByDeviceId(String deviceId);
         */
    List<String> getAllSoftwareNames();

}
