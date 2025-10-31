package org.licensetracker.service;

import org.licensetracker.dto.DashboardMetricsDTO;
import org.licensetracker.dto.LicenseAlertDTO;

import java.util.List;

public interface DashboardService {
    DashboardMetricsDTO getDashboardMetrics();
    List<LicenseAlertDTO> getExpiringLicenses(int days);
    long getDevicesAtRisk(int days);
}
