package com.swd.smk.repository;

import com.swd.smk.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Custom query methods can be defined here if needed\
    List<Transaction> findByMemberId(Long memberId);
}
