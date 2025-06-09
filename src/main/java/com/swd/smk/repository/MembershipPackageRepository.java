package com.swd.smk.repository;

import com.swd.smk.model.MembershipPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipPackageRepository extends JpaRepository<MembershipPackage, Long> {
    // Additional query methods can be defined here if needed

}
