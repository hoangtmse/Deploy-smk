package com.swd.smk.controller;

import com.swd.smk.dto.MemberShipPackageDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.model.MembershipPackage;
import com.swd.smk.services.interfac.IMembershipPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MembershipPackageController {

    @Autowired
    private IMembershipPackage membershipPackageService;

    @GetMapping("/public/get-membership-package-by-id/{id}")
    public ResponseEntity<Response> getMembershipPackageById(@PathVariable Long id) {
        Response response = membershipPackageService.getMembershipPackageById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/public/get-all-membership-packages")
    public ResponseEntity<Response> getAllMembershipPackages() {
        Response response = membershipPackageService.getAllMembershipPackages();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/public/create-membership-package")
    public ResponseEntity<Response> createMembershipPackage(@RequestBody MemberShipPackageDTO membershipPackageDTO) {
        Response response = membershipPackageService.createMembershipPackage(membershipPackageDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/public/update-membership-package/{id}")
    public ResponseEntity<Response> updateMembershipPackage(@PathVariable Long id, @RequestBody MemberShipPackageDTO membershipPackageDTO) {
        Response response = membershipPackageService.updateMembershipPackage(id, membershipPackageDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/public/delete-membership-package/{id}")
    public ResponseEntity<Response> deleteMembershipPackage(@PathVariable Long id) {
        Response response = membershipPackageService.deleteMembershipPackage(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
