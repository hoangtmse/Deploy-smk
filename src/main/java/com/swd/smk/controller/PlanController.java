package com.swd.smk.controller;

import com.swd.smk.dto.*;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.services.interfac.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlanController {

    @Autowired
    private IPlanService planService;

    @Autowired
    private IPlanWeek planWeekService;

    @Autowired
    private IPlanDay planDayService;

    @Autowired
    private IPlanPhase planPhaseService;

    @Autowired
    private ICopingMechanism copingMechanismService;

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

    @GetMapping("/user/get-plan-week-by-plan/{planId}")
    public ResponseEntity<Response> getPlanWeekByPlanId(@PathVariable Long planId) {
        Response response = planWeekService.getPlanWeekByPlanId(planId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/user/update-plan-day/week/{weekId}/day/{dayId}")
    public ResponseEntity<Response> updatePlanDay(
            @PathVariable Long weekId,
            @PathVariable Long dayId,
            @RequestBody PlanDayDTO planDayDTO) {
        Response response = planDayService.updatePlanDay(weekId, dayId, planDayDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/user/update-plan-phase/{planPhaseId}")
    public ResponseEntity<Response> updatePlanPhase(
            @PathVariable Long planPhaseId,
            @RequestBody PlanPhaseDTO planDTO) {
        Response response = planPhaseService.UpdatePlanPhase(planPhaseId, planDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/user/update-coping-mechanism/{copingMechanismId}/plan/{planId}")
    public ResponseEntity<Response> updateCopingMechanism(
            @PathVariable Long copingMechanismId,
            @PathVariable Long planId,
            @RequestBody CopingMechanismDTO copingMechanismDTO) {
        Response response = copingMechanismService.updateCopingMechanism(copingMechanismId, planId, copingMechanismDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
