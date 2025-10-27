package org.licensetracker.repository;

import org.licensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String username);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);
}
