package com.swd.smk.controller;

import com.swd.smk.dto.MemberShipPackageDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.MembershipPackage;
import com.swd.smk.repository.MembershipPackageRepository;
import com.swd.smk.services.interfac.IMembershipPackage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MembershipPackageController {

    @Autowired
    private IMembershipPackage membershipPackageService;

    @Autowired
    private MembershipPackageRepository membershipPackageRepository;

    @GetMapping("/user/get-membership-package-by-id/{id}")
    public ResponseEntity<Response> getMembershipPackageById(@PathVariable Long id) {
        Response response = membershipPackageService.getMembershipPackageById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-all-membership-packages")
    public ResponseEntity<Response> getAllMembershipPackages() {
        Response response = membershipPackageService.getAllMembershipPackages();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/admin/create-membership-package")
    public ResponseEntity<Response> createMembershipPackage(@RequestBody MemberShipPackageDTO membershipPackageDTO) {
        Response response = membershipPackageService.createMembershipPackage(membershipPackageDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/admin/update-membership-package/{id}")
    public ResponseEntity<Response> updateMembershipPackage(@PathVariable Long id, @RequestBody MemberShipPackageDTO membershipPackageDTO) {
        Response response = membershipPackageService.updateMembershipPackage(id, membershipPackageDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-membership-package/{id}")
    public ResponseEntity<Response> deleteMembershipPackage(@PathVariable Long id) {
        Response response = membershipPackageService.deleteMembershipPackage(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/user/buy-membership-package/{id}/member/{memberId}")
    public ResponseEntity<Response> buyMembershipPackage(@PathVariable Long id, @PathVariable Long memberId, HttpServletRequest request) {
        Response response = membershipPackageService.buyMembershipPackage(memberId, id, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
