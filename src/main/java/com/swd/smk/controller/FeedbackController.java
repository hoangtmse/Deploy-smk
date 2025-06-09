package com.swd.smk.controller;

import com.swd.smk.dto.FeedBackDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class FeedbackController {

    @Autowired
    private IFeedbackService feedbackService;

    @PostMapping("/create-feedback/member/{memberId}")
    public ResponseEntity<Response> createFeedback(@PathVariable Long memberId, @RequestBody FeedBackDTO feedBackDTO) {
        Response response = feedbackService.createFeedback(memberId, feedBackDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-feedback/{feedbackId}")
    public ResponseEntity<Response> updateFeedback(@PathVariable Long feedbackId, @RequestBody FeedBackDTO feedBackDTO) {
        Response response = feedbackService.updateFeedback(feedbackId, feedBackDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-feedback/{feedbackId}")
    public ResponseEntity<Response> deleteFeedback(@PathVariable Long feedbackId) {
        Response response = feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-feedback/{feedbackId}")
    public ResponseEntity<Response> getFeedbackById(@PathVariable Long feedbackId) {
        Response response = feedbackService.getFeedbackById(feedbackId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-all-feedbacks")
    public ResponseEntity<Response> getAllFeedbacks() {
        Response response = feedbackService.getAllFeedbacks();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
