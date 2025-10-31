package org.licensetracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
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
