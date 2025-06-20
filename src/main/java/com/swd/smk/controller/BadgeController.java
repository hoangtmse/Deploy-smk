package com.swd.smk.controller;

import com.swd.smk.services.interfac.IBadgeService;
import com.swd.smk.dto.BadgeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BadgeController {

    @Autowired
    private IBadgeService badgeService;

    @PostMapping("/admin/create-badge")
    public ResponseEntity<com.swd.smk.dto.Response> createBadge(@RequestBody BadgeDTO badgeDTO) {
        com.swd.smk.dto.Response response = badgeService.createBadge(badgeDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/admin/update-badge/{id}")
    public ResponseEntity<com.swd.smk.dto.Response> updateBadge(@PathVariable Long id, @RequestBody BadgeDTO badgeDTO) {
        com.swd.smk.dto.Response response = badgeService.updateBadge(id, badgeDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-badge/{id}")
    public ResponseEntity<com.swd.smk.dto.Response> deleteBadge(@PathVariable Long id) {
        com.swd.smk.dto.Response response = badgeService.deleteBadge(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-badge/{id}")
    public ResponseEntity<com.swd.smk.dto.Response> getBadgeById(@PathVariable Long id) {
        com.swd.smk.dto.Response response = badgeService.getBadgeById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-all-badges")
    public ResponseEntity<com.swd.smk.dto.Response> getAllBadges() {
        com.swd.smk.dto.Response response = badgeService.getAllBadges();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
