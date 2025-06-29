package com.swd.smk.controller;

import com.swd.smk.config.CloudinaryService;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.IBadgeService;
import com.swd.smk.dto.BadgeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class BadgeController {

    @Autowired
    private IBadgeService badgeService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/admin/create-badge")
    public ResponseEntity<Response> createBadge(@RequestBody BadgeDTO badgeDTO) {
        Response response = badgeService.createBadge(badgeDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/admin/update-badge/{id}")
    public ResponseEntity<Response> updateBadge(@PathVariable Long id, @RequestBody BadgeDTO badgeDTO) {
        Response response = badgeService.updateBadge(id, badgeDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-badge/{id}")
    public ResponseEntity<Response> deleteBadge(@PathVariable Long id) {
        Response response = badgeService.deleteBadge(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-badge/{id}")
    public ResponseEntity<Response> getBadgeById(@PathVariable Long id) {
        Response response = badgeService.getBadgeById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-all-badges")
    public ResponseEntity<Response> getAllBadges() {
        Response response = badgeService.getAllBadges();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/admin/upload-badge-image/{id}")
    public ResponseEntity<Response> uploadBadgeImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            Response response = badgeService.updateBadgeImage(id, imageUrl);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            Response error = new Response();
            error.setStatusCode(500);
            error.setMessage("Upload failed: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
