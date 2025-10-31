package org.licensetracker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardMetricsDTO {
    private long totalLicenses;
    private long totalDevices;
    private long licensesExpiringSoon;
}
