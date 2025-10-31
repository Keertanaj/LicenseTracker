package org.licensetracker.repository;

import org.licensetracker.dto.ReportDTO;
import org.licensetracker.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByLicense_LicenseKey(String licenseKey);
    List<Assignment> findByDevice_DeviceId(String deviceId);
    List<Assignment> findByLicense_LicenseKeyIn(List<String> licenseKeys);
    @Query("SELECT COUNT(*) FROM Assignment a WHERE a.license.licenseKey = :licenseKey")
    Long countAssignmentsByLicenseKey(@Param("licenseKey") String licenseKey);
    long count();

    @Query("SELECT new org.licensetracker.dto.ReportDTO(" +
            "a.license.licenseKey, a.device.deviceId, a.license.softwareName, " +
            "a.license.vendor.vendorName, a.device.location, a.license.validTo) " +
            "FROM Assignment a " +
            "WHERE (:vendor IS NULL OR a.license.vendor.vendorName = :vendor) " +
            "AND (:software IS NULL OR a.license.softwareName = :software) " +
            "AND (:location IS NULL OR a.device.location = :location)")
    List<ReportDTO> findReportData(String vendor, String software, String location);
}
