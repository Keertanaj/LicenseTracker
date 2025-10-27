package org.licensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseResponseDTO {
    private String licenseKey;
    private String vendor;
    private String softwareName;
    private String licenseType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer maxUsage;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
