package org.licensetracker.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class VendorRequestDTO {
    @NotBlank(message = "Vendor Name is required")
    @Size(max = 100, message = "Vendor Name must be less than 100 characters")
    private String vendorName;

    @Email(message = "Support Email must be valid")
    @Size(max = 100, message = "Support Email must be less than 100 characters")
    private String supportEmail;
}
