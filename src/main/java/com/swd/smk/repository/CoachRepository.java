package com.swd.smk.repository;

import com.swd.smk.model.Coach;
import com.swd.smk.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByUsername(String username);
    Optional<Coach> findByUsernameAndStatus(String username, Status status);
    Optional<Coach> findByEmailAndStatus(String email, Status status);
}
