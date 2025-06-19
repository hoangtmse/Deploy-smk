package com.swd.smk.controller;

import com.swd.smk.dto.AdminDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @PostMapping("/public/login-admin")
    public ResponseEntity<Response> login(@RequestBody AdminDTO loginRequest) {
        Response response = adminService.loginAdmin(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/public/register-admin")
    public ResponseEntity<Response> register(@RequestBody AdminDTO request) {
        Response response = adminService.registerAdmin(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/get-all-admins")
    public ResponseEntity<Response> getAll() {
        Response response = adminService.getAllAdmins();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/get-admin-by-id/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        Response response = adminService.getAdminById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-admin/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = adminService.deleteAdmin(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/admin/update-admin/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody AdminDTO request) {
        Response response = adminService.updateAdmin(id, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
