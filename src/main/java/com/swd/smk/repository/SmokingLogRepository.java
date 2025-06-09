package com.swd.smk.repository;

import com.swd.smk.model.SmokingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmokingLogRepository extends JpaRepository<SmokingLog, Long> {
    // Additional query methods can be defined here if needed
}
