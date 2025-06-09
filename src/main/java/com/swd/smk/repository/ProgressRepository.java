package com.swd.smk.repository;

import com.swd.smk.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    // Additional query methods can be defined here if needed
}
