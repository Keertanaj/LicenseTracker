package org.licensetracker.service;

import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.dto.LicenseRequestDTO;
import org.licensetracker.dto.LicenseResponseDTO;

import java.util.List;

public interface LicenseService {
    LicenseResponseDTO addLicense(LicenseRequestDTO request);
    List<LicenseResponseDTO> listLicenses();
    LicenseResponseDTO updateLicense(String licenseKey, LicenseRequestDTO request);
    String deleteLicense(String licenseKey);
    List<LicenseResponseDTO> searchLicenses(String vendor, String softwareName);
    List<LicenseAlertDTO> getExpiringLicenses(int days);
}
