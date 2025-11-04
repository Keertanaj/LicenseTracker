package org.licensetracker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.licensetracker.entity.SoftwareStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SoftwareResponseDTO {
    private Integer id;
    private String softwareName;
    private String currentVersion;
    private String latestVersion;
    private SoftwareStatus status;
    private LocalDate lastChecked;
}
