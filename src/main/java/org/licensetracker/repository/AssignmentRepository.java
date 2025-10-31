package org.licensetracker.repository;

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
}
