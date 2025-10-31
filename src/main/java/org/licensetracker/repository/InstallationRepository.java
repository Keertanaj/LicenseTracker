package org.licensetracker.repository;

import org.licensetracker.entity.Installation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallationRepository extends JpaRepository<Installation, Integer> {
}
