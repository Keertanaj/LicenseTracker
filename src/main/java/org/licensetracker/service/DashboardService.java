package org.licensetracker.service;

import org.licensetracker.dto.DashboardMetricsDTO;
import org.licensetracker.dto.LicenseAlertDTO;

import java.util.List;

public interface DashboardService {
    // Modified to accept an optional softwareName parameter
    DashboardMetricsDTO getDashboardMetrics();
    List<LicenseAlertDTO> getExpiringLicenses(int days);
    long getDevicesAtRisk(int days);
}
