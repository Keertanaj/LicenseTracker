package org.licensetracker.utility;

import org.licensetracker.dto.DashboardMetricsDTO;

public class DashboardUtility {

    public static DashboardMetricsDTO toDto(long totalLicenses, long totalDevices, long licensesExpiringSoon) {
        return DashboardMetricsDTO.builder()
                .totalLicenses(totalLicenses)
                .totalDevices(totalDevices)
                .licensesExpiringSoon(licensesExpiringSoon)
                .build();
    }
}
