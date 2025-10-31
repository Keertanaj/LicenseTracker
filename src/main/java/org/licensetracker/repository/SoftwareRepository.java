package org.licensetracker.repository;

import org.licensetracker.entity.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Integer> {

    @Query("SELECT DISTINCT s.softwareName FROM Software s ORDER BY s.softwareName")
    List<String> findDistinctSoftwareNames();

    Optional<Software> findBySoftwareName(String softwareName);
}
