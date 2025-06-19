package com.swd.smk.repository;

import com.swd.smk.model.MembershipPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipPackageRepository extends JpaRepository<MembershipPackage, Long> {
    // Additional query methods can be defined here if needed
    @Query("SELECT CASE WHEN COUNT(mp) > 0 THEN true ELSE false END FROM MembershipPackage mp JOIN mp.members m WHERE m.id = :memberId")
    boolean existsByMemberId(Long memberId);
}
