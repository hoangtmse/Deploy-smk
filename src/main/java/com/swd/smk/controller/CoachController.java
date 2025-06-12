package com.swd.smk.controller;

import com.swd.smk.dto.CoachDTO;
import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.ICoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CoachController {

    @Autowired
    private ICoachService coachService;

    @PostMapping("/public/login-coach")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = coachService.loginCoach(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/admin/register-coach")
    public ResponseEntity<Response> register(@RequestBody CoachDTO request) {
        Response response = coachService.registerCoach(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("user/get-all-coaches")
    public ResponseEntity<Response> getAll() {
        Response response = coachService.getAllCoaches();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("user/get-coach-by-id/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        Response response = coachService.getCoachById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-coach/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = coachService.deleteCoach(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/coach/update-coach/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody CoachDTO request) {
        Response response = coachService.updateCoach(id, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
