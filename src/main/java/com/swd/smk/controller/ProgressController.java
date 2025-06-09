package com.swd.smk.controller;

import com.swd.smk.dto.ProgressDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.IProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProgressController {

    @Autowired
    private IProgressService progressService;

    @PostMapping("/public/create-progress/member/{memberId}")
    public ResponseEntity<Response> createProgress(@RequestBody ProgressDTO progressDTO, @PathVariable Long memberId) {
        Response response = new Response();
        progressDTO.setMemberId(memberId);
        response = progressService.createProgress(progressDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/public/update-progress/{progressId}")
    public ResponseEntity<Response> updateProgress(@PathVariable Long progressId, @RequestBody ProgressDTO progressDTO) {
        Response response = progressService.updateProgress(progressId, progressDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/public/delete-progress/{progressId}")
    public ResponseEntity<Response> deleteProgress(@PathVariable Long progressId) {
        Response response = progressService.deleteProgress(progressId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/public/get-progress/{progressId}")
    public ResponseEntity<Response> getProgressById(@PathVariable Long progressId) {
        Response response = progressService.getProgressById(progressId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/public/get-all-progresses")
    public ResponseEntity<Response> getAllProgresses() {
        Response response = progressService.getAllProgresses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
