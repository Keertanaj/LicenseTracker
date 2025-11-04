package org.licensetracker.utility;


import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.dto.LicenseRequestDTO;
import org.licensetracker.dto.LicenseResponseDTO;
import org.licensetracker.entity.License;
import org.licensetracker.entity.Vendor;
import org.licensetracker.repository.VendorRepository;

public class LicenseUtility {

    public static License toEntity(LicenseRequestDTO dto, VendorRepository vendorRepository) {
        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid vendor ID: " + dto.getVendorId()));

        return License.builder()
                .licenseKey(dto.getLicenseKey())
                .vendor(vendor)
                .softwareName(dto.getSoftwareName())
                .licenseType(dto.getLicenseType())
                .validFrom(dto.getValidFrom())
                .validTo(dto.getValidTo())
                .maxUsage(dto.getMaxUsage())
                .notes(dto.getNotes())
                .build();
    }

    public static LicenseResponseDTO toDto(License l) {
        return LicenseResponseDTO.builder()
                .licenseKey(l.getLicenseKey())
                .vendorName(l.getVendor() != null ? l.getVendor().getVendorName() : null)
                .softwareName(l.getSoftwareName())
                .licenseType(l.getLicenseType() != null ? l.getLicenseType().name() : null)
                .validFrom(l.getValidFrom())
                .validTo(l.getValidTo())
                .maxUsage(l.getMaxUsage())
                .notes(l.getNotes())
                .createdAt(l.getCreatedAt())
                .updatedAt(l.getUpdatedAt())
                .build();
    }

    public static LicenseAlertDTO toAlertDto(License l) {
        return LicenseAlertDTO.builder()
                .licenseKey(l.getLicenseKey())
                .softwareName(l.getSoftwareName())
                .vendor(l.getVendor().getVendorName())
                .validTo(l.getValidTo())
                .build();
    }

    public static void updateEntityFromDto(License l, LicenseRequestDTO dto, VendorRepository vendorRepository) {
        if (dto.getVendorId() != null) {
            Vendor vendor = vendorRepository.findById(dto.getVendorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid vendor ID: " + dto.getVendorId()));
            l.setVendor(vendor);
        }
        if (dto.getSoftwareName() != null) l.setSoftwareName(dto.getSoftwareName());
        if (dto.getLicenseType() != null) l.setLicenseType(dto.getLicenseType());
        if (dto.getValidFrom() != null) l.setValidFrom(dto.getValidFrom());
        if (dto.getValidTo() != null) l.setValidTo(dto.getValidTo());
        if (dto.getMaxUsage() != null) l.setMaxUsage(dto.getMaxUsage());
        if (dto.getNotes() != null) l.setNotes(dto.getNotes());
    }
}