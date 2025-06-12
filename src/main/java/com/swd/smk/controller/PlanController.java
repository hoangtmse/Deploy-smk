package com.swd.smk.controller;

import com.swd.smk.dto.PlanDTO;
import com.swd.smk.dto.Response;
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

//    @PostMapping("/public/create-plan/member/{memberId}")
//    public ResponseEntity<Response>  createPlan(@RequestBody PlanDTO planDTO, @PathVariable Long memberId) {
//        planDTO.setMemberId(memberId);
//      Response response = planService.createPlan(planDTO);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }

    @PutMapping("/public/update-plan/{planId}")
    public ResponseEntity<Response> updatePlan(@PathVariable Long planId, @RequestBody PlanDTO planDTO) {
        Response response = planService.updatePlan(planId, planDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/public/delete-plan/{planId}")
    public ResponseEntity<Response> deletePlan(@PathVariable Long planId) {
        Response response = planService.deletePlan(planId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/public/get-plan/{planId}")
    public ResponseEntity<Response> getPlanById(@PathVariable Long planId) {
        Response response = planService.getPlanById(planId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/public/get-all-plans")
    public ResponseEntity<Response> getAllPlans() {
        Response response = planService.getAllPlans();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
