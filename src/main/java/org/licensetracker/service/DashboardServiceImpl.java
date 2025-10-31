package org.licensetracker.service;

import org.licensetracker.dto.DashboardMetricsDTO;
import org.licensetracker.dto.LicenseAlertDTO;
import org.licensetracker.entity.Assignment;
import org.licensetracker.entity.Device;
import org.licensetracker.repository.AssignmentRepository;
import org.licensetracker.repository.DeviceRepo;
import org.licensetracker.repository.LicenseRepository;
import org.licensetracker.utility.DashboardUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private LicenseService licenseService;

    @Override
    public DashboardMetricsDTO getDashboardMetrics() {
        long totalLicenses = licenseRepository.count();
        long totalDevices = deviceRepo.count();
        long licensesExpiringSoon = licenseRepository.countExpiringLicenses(LocalDate.now().plusDays(30)).size();

        return DashboardUtility.toDto(totalLicenses, totalDevices, licensesExpiringSoon);
    }

    @Override
    public List<LicenseAlertDTO> getExpiringLicenses(int days) {
        return licenseService.getExpiringLicenses(days);
    }

    @Override
    public long getDevicesAtRisk(int days) {
        List<LicenseAlertDTO> expiringLicenses = licenseService.getExpiringLicenses(days);
        Set<String> expiringLicenseKeys = expiringLicenses.stream()
                .map(LicenseAlertDTO::getLicenseKey)
                .collect(Collectors.toSet());

        Set<Device> devicesAtRisk = expiringLicenseKeys.stream()
                .flatMap(licenseKey -> assignmentRepository.findByLicense_LicenseKey(licenseKey).stream())
                .map(Assignment::getDevice)
                .collect(Collectors.toSet());

        return devicesAtRisk.size();
    }
}
