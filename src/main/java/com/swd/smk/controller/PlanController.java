package com.swd.smk.controller;

import com.swd.smk.dto.PlanDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.dto.SmokingLogDTO;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.services.interfac.IPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlanController {

    @Autowired
    private IPlanService planService;

    @PostMapping("/user/create-plan/member/{memberId}/smoking-log/{smokingLogId}")
    public ResponseEntity<Response>  createPlan(@PathVariable Long smokingLogId, @PathVariable Long memberId) {
      Response response = planService.createPlan(memberId, smokingLogId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/user/delete-plan/{planId}")
    public ResponseEntity<Response> deletePlan(@PathVariable Long planId) {
        Response response = planService.deletePlan(planId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-plan/{planId}")
    public ResponseEntity<Response> getPlanById(@PathVariable Long planId) {
        Response response = planService.getPlanById(planId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/admin/get-all-plans")
    public ResponseEntity<Response> getAllPlans() {
        Response response = planService.getAllPlans();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-plans-by-member/{memberId}")
    public ResponseEntity<Response> getPlansByMemberId(@PathVariable Long memberId) {
        Response response = planService.getPlansByMemberId(memberId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
