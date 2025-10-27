package org.licensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.licensetracker.entity.LicenseType;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseRequestDTO {
    private String licenseKey;
    private String vendor;
    private String softwareName;
    private LicenseType licenseType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer maxUsage;
    private String notes;
}
