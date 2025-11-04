package org.licensetracker.service;

import org.licensetracker.dto.VendorRequestDTO;
import org.licensetracker.dto.VendorResponseDTO;

import java.util.List;

public interface VendorService {

    VendorResponseDTO addVendor(VendorRequestDTO request);
    VendorResponseDTO updateVendor(Long vendorId, VendorRequestDTO request);
    void deleteVendor(Long vendorId);
    VendorResponseDTO getVendorById(Long vendorId);
    List<VendorResponseDTO> getAllVendors();
}
