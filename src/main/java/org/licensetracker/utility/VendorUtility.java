package org.licensetracker.utility;

import org.licensetracker.dto.VendorRequestDTO;
import org.licensetracker.dto.VendorResponseDTO;
import org.licensetracker.entity.Vendor;

public class VendorUtility {

    public static Vendor toEntity(VendorRequestDTO dto) {
        return new Vendor(null, dto.getVendorName(), dto.getSupportEmail());
    }

    public static VendorResponseDTO toDto(Vendor vendor) {
        return VendorResponseDTO.builder()
                .vendorId(vendor.getVendorId())
                .vendorName(vendor.getVendorName())
                .supportEmail(vendor.getSupportEmail())
                .build();
    }

    public static void updateEntityFromDto(Vendor existing, VendorRequestDTO dto) {
        existing.setVendorName(dto.getVendorName());
        existing.setSupportEmail(dto.getSupportEmail());
    }
}