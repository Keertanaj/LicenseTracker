package org.licensetracker.service;

import org.licensetracker.dto.ReportDTO;
import org.licensetracker.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public List<ReportDTO> generateReport(String vendor, String software, String location) {
        return assignmentRepository.findReportData(vendor, software, location);
    }
}
