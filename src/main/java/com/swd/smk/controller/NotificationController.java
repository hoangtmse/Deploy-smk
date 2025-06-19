package com.swd.smk.controller;

import com.swd.smk.dto.NotificationDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @PostMapping("/user/create-notification/member/{memberId}")
    public ResponseEntity<Response> createNotification(@RequestBody NotificationDTO notificationDTO,
                                                       @PathVariable Long memberId) {
        notificationDTO.setMemberId(memberId);
        Response response = notificationService.createNotification(notificationDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/user/update-notification/{notificationId}")
    public ResponseEntity<Response> updateNotification(@PathVariable Long notificationId, @RequestBody NotificationDTO notificationDTO) {
        Response response = notificationService.updateNotification(notificationId, notificationDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/user/delete-notification/{notificationId}")
    public ResponseEntity<Response> deleteNotification(@PathVariable Long notificationId) {
        Response response = notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-notification/{notificationId}")
    public ResponseEntity<Response> getNotificationById(@PathVariable Long notificationId) {
        Response response = notificationService.getNotificationById(notificationId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-all-notifications")
    public ResponseEntity<Response> getAllNotifications() {
        Response response = notificationService.getAllNotifications();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

