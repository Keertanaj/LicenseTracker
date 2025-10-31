package org.licensetracker.dto;

import lombok.*;
import org.licensetracker.entity.LicenseType;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseRequestDTO {
    private String licenseKey;
    private Long vendorId;
    private String softwareName;
    private LicenseType licenseType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer maxUsage;
    private String notes;
}
