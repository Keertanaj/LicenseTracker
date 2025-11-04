package org.licensetracker.repository;

import org.licensetracker.entity.Installation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallationRepository extends JpaRepository<Installation, Integer> {

    // New method to count distinct devices for a given software name
    @Query("SELECT COUNT(DISTINCT i.device) FROM Installation i WHERE i.software.softwareName = :softwareName")
    long countDistinctDevicesBySoftwareName(@Param("softwareName") String softwareName);
}
