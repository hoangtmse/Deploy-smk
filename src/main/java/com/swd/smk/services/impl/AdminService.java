package com.swd.smk.services.impl;

import com.swd.smk.dto.AdminDTO;
import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Role;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Admin;
import com.swd.smk.repository.AdminRepository;
import com.swd.smk.services.interfac.IAdminService;
import com.swd.smk.utils.Converter;
import com.swd.smk.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response registerAdmin(AdminDTO adminRequest) {
        Response response = new Response();
        try {
            if (adminRepository.findByUsername(adminRequest.getUsername().trim()).isPresent()) {
                throw new OurException("Username already existed");
            }
            String encodedPassword = passwordEncoder.encode(adminRequest.getPassword().trim());
            Admin admin = new Admin();
            admin.setUsername(adminRequest.getUsername().trim());
            admin.setPassword(encodedPassword);
            admin.setRole(adminRequest.getRole() != null ? adminRequest.getRole() : Role.ADMIN);
            admin.setStatus(Status.ACTIVE);
            admin.setDateCreated(LocalDate.now());
            admin.setDateUpdated(LocalDate.now());
            adminRepository.save(admin);
            response.setStatusCode(200);
            response.setMessage("Registered successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response loginAdmin(AdminDTO loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()));
            var admin = adminRepository.findByUsernameAndStatus(loginRequest.getUsername().trim(), Status.ACTIVE)
                    .orElseThrow(() -> new OurException("Admin not found"));
            var token = jwtUtils.generateToken(admin);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(admin.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }   

    @Override
    public Response getAllAdmins() {
        Response response = new Response();
        try {
            List<Admin> admins = adminRepository.findAll();
            List<AdminDTO> dtos = admins.stream()
                    .map(Converter::convertAdminToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setAdmins(dtos);
            response.setMessage("Fetched all admins");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteAdmin(Long adminId) {
        Response response = new Response();
        try {
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new OurException("Admin not found"));
            admin.setStatus(Status.DELETED);
            adminRepository.save(admin);

            response.setStatusCode(200);
            response.setMessage("Deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAdminById(Long adminId) {
        Response response = new Response();
        try {
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new OurException("Admin not found"));
            response.setStatusCode(200);
            response.setAdmin(Converter.convertAdminToDTO(admin));
            response.setMessage("Fetched admin by id");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateAdmin(Long adminId, AdminDTO adminDTO) {
        Response response = new Response();
        try {
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new OurException("Admin not found"));

            if (adminDTO.getUsername() != null && !adminDTO.getUsername().trim().equals(admin.getUsername())) {
                if (adminRepository.findByUsername(adminDTO.getUsername().trim()).isPresent()) {
                    throw new OurException("Username already existed");
                }
                admin.setUsername(adminDTO.getUsername().trim());
            }
            admin.setDateUpdated(LocalDate.now());
            if (adminDTO.getPassword() != null) admin.setPassword(passwordEncoder.encode(adminDTO.getPassword().trim()));

            adminRepository.save(admin);
            response.setStatusCode(200);
            response.setMessage("Updated successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }
}


