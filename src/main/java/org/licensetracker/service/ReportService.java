package org.licensetracker.service;

import org.licensetracker.dto.ReportDTO;

import java.util.List;

public interface ReportService {
    public List<ReportDTO> generateReport(String vendor, String software, String location);
}
