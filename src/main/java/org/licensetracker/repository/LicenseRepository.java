package org.licensetracker.repository;

import org.licensetracker.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, String> {
    List<License> findBySoftwareNameContainingIgnoreCase(String softwareName);
    List<License> findByVendorIgnoreCase(String vendor);
}