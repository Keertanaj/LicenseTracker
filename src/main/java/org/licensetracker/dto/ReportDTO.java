package org.licensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String licenseKey;
    private String deviceId;
    private String softwareName;
    private String vendorName;
    private String location;
    private LocalDate expiryDate;
}
