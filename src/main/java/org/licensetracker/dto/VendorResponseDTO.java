package org.licensetracker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VendorResponseDTO {
    private Long vendorId;
    private String vendorName;
    private String supportEmail;
}
