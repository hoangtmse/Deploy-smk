package com.swd.smk.services.interfac;

import com.swd.smk.dto.MemberShipPackageDTO;
import com.swd.smk.dto.Response;
import jakarta.servlet.http.HttpServletRequest;

public interface IMembershipPackage {
    Response getMembershipPackageById(Long id);
    Response getAllMembershipPackages();
    Response createMembershipPackage( MemberShipPackageDTO memberShipPackageDTO);
    Response updateMembershipPackage(Long id, MemberShipPackageDTO memberShipPackageDTO);
    Response deleteMembershipPackage(Long id);
    Response buyMembershipPackage(Long id, Long memberId, HttpServletRequest request);
    boolean checkMembershipPackage(Long memberId);
}
