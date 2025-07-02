package com.swd.smk.repository;

import com.swd.smk.model.Badge;
import com.swd.smk.model.jointable.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    @Query("SELECT mb.badge FROM MemberBadge mb WHERE mb.member.id = :memberId AND mb.status = 'ACTIVE'")
    List<Badge> findBadgesByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndBadgeId(Long id, Long id1);
}
