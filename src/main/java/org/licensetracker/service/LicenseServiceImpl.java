package org.licensetracker.service;

import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.dto.LicenseRequestDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.entity.License;
import org.licensetracker.exception.DuplicateResourceException;
import org.licensetracker.exception.ResourceNotFoundException;
import org.licensetracker.repository.LicenseRepository;
import org.licensetracker.utility.LicenseUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    private LicenseRepository licenseRepo;

    @Override
    public LicenseResponseDTO addLicense(LicenseRequestDTO request) {
        if (licenseRepo.existsById(request.getLicenseKey())) {
            throw new DuplicateResourceException("License already exists: " + request.getLicenseKey());
        }
        License license = LicenseUtility.toEntity(request);
        License saved = licenseRepo.save(license);
        return LicenseUtility.toDto(saved);
    }

    @Override
    public List<LicenseResponseDTO> listLicenses() {
        return licenseRepo.findAll()
                .stream()
                .map(LicenseUtility::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LicenseResponseDTO updateLicense(String licenseKey, LicenseRequestDTO request) {
        License license = licenseRepo.findById(licenseKey)
                .orElseThrow(() -> new ResourceNotFoundException("License not found: " + licenseKey));

        LicenseUtility.updateEntityFromDto(license, request);
        License saved = licenseRepo.save(license);
        return LicenseUtility.toDto(saved);
    }

    @Override
    public String deleteLicense(String licenseKey) {
        License license = licenseRepo.findById(licenseKey)
                .orElseThrow(() -> new ResourceNotFoundException("License not found: " + licenseKey));

        licenseRepo.delete(license);
        return "License deleted successfully";
    }

    @Override
    public List<LicenseResponseDTO> searchLicenses(String vendor, String softwareName) {
        List<License> licenses;

        if (vendor != null && !vendor.isEmpty()) {
            licenses = licenseRepo.findByVendorIgnoreCase(vendor);
        } else if (softwareName != null && !softwareName.isEmpty()) {
            licenses = licenseRepo.findBySoftwareNameContainingIgnoreCase(softwareName);
        } else {
            licenses = licenseRepo.findAll();
        }

        return licenses.stream().map(LicenseUtility::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LicenseAlertDTO> getExpiringLicenses(int days) {
        LocalDate thresholdDate = LocalDate.now().plusDays(days);
        List<License> expiringLicenses = licenseRepo.findExpiringLicenses(days);
        return expiringLicenses.stream()
                .map(LicenseUtility::toAlertDto)
                .collect(Collectors.toList());
    }
}