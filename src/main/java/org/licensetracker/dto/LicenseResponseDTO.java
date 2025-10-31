package org.licensetracker.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseResponseDTO {
    private String licenseKey;
    private String vendorName;
    private String softwareName;
    private String licenseType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer maxUsage;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
