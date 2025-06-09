package com.swd.smk.controller;

import com.swd.smk.dto.Response;
import com.swd.smk.dto.SmokingLogDTO;
import com.swd.smk.services.interfac.ISmokingLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SmokingLogController {

    @Autowired
    private ISmokingLog smokingLogService;

    @PostMapping("public/create-smoking-log/member/{memberId}")
    public ResponseEntity<Response> createSmokingLog(@RequestBody SmokingLogDTO smokingLogDTO,
                                                     @PathVariable Long memberId) {
        smokingLogDTO.setMemberId(memberId);
        Response response = smokingLogService.createSmokingLog(smokingLogDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("public/update-smoking-log/{smokingLogId}")
    public ResponseEntity<Response> updateSmokingLog(@PathVariable Long smokingLogId,
                                                     @RequestBody SmokingLogDTO smokingLogDTO) {
        Response response = smokingLogService.updateSmokingLog(smokingLogId, smokingLogDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("public/delete-smoking-log/{smokingLogId}")
    public ResponseEntity<Response> deleteSmokingLog(@PathVariable Long smokingLogId) {
        Response response = smokingLogService.deleteSmokingLog(smokingLogId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("public/get-smoking-log/{smokingLogId}")
    public ResponseEntity<Response> getSmokingLogById(@PathVariable Long smokingLogId) {
        Response response = smokingLogService.getSmokingLogById(smokingLogId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("public/get-all-smoking-logs")
    public ResponseEntity<Response> getAllSmokingLogs() {
        Response response = smokingLogService.getAllSmokingLogs();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
