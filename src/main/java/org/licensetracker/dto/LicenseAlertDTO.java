package org.licensetracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseAlertDTO {
    private String licenseKey;
    private String softwareName;
    private String vendor;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validTo;
}
