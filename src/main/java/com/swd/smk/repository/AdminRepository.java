package com.swd.smk.repository;

import com.swd.smk.model.Admin;
import com.swd.smk.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByUsernameAndStatus(String username, Status status);
}
