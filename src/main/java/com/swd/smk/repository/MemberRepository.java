package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndStatus(String email, Status status);

    Optional<Member> findByUsernameAndStatus(String username, Status status);

    Optional<Member> findByIdAndStatus(Long id, Status status);
}
