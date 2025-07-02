package com.swd.smk.repository;

import com.swd.smk.model.Badge;
import com.swd.smk.model.jointable.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    List<Badge> findBadgesByMemberId(Long memberId);

    boolean existsByMemberIdAndBadgeId(Long id, Long id1);
}
