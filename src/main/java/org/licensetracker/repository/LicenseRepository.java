package org.licensetracker.repository;

import org.licensetracker.entity.License;
import org.licensetracker.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, String> {
    List<License> findBySoftwareNameContainingIgnoreCase(String softwareName);
    List<License> findByVendor(Vendor vendor);
    List<License> findByVendorVendorNameIgnoreCaseAndSoftwareNameContainingIgnoreCase(String vendorName, String softwareName);
    @Query(value = "SELECT * FROM license WHERE valid_to <= DATE_ADD(CURRENT_DATE(), INTERVAL :days DAY)", nativeQuery = true)
    List<License> findExpiringLicenses(@Param("days") int days);

    @Query("SELECT l FROM License l WHERE l.validTo <= :date")
    List<License> countExpiringLicenses(@Param("date") java.time.LocalDate date);

    @Query("SELECT DISTINCT l.softwareName FROM License l")
    List<String> findDistinctSoftware();
}