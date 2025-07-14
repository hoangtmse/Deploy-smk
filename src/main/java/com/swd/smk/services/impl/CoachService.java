package com.swd.smk.services.impl;

import com.swd.smk.dto.CoachDTO;
import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Role;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Coach;
import com.swd.smk.repository.CoachRepository;
import com.swd.smk.services.interfac.ICoachService;
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
public class CoachService implements ICoachService {
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response registerCoach(CoachDTO coachRequest) {
        Response response = new Response();
        try {
            if (coachRepository.findByEmailAndStatus(coachRequest.getEmail().trim(), Status.ACTIVE).isPresent()) {
                throw new OurException("Email already existed");
            }
            if (coachRepository.findByUsernameAndStatus(coachRequest.getUsername().trim(), Status.ACTIVE).isPresent()) {
                throw new OurException("Username already existed");
            }
            String encodedPassword = passwordEncoder.encode(coachRequest.getPassword().trim());
            
            if (coachRequest.getDob() == null) {
                throw new OurException("Date of birth is required");
            }
            
            if (coachRequest.getDob().isAfter(java.time.LocalDate.now())) {
                throw new OurException("Date of birth cannot be in the future");
            }
            
            if (coachRequest.getPhoneNumber() == null || coachRequest.getPhoneNumber().trim().isEmpty()) {
                throw new OurException("Phone number is required");
            }
            
            if (coachRequest.getPhoneNumber().trim().length() < 10 || coachRequest.getPhoneNumber().trim().length() > 15) {
                throw new OurException("Phone number must be between 10 and 15 characters");
            }

            Coach coach = new Coach();
            coach.setEmail(coachRequest.getEmail().trim());
            coach.setUsername(coachRequest.getUsername().trim());
            coach.setName(coachRequest.getName().trim());
            coach.setPassword(encodedPassword);
            coach.setExpertise(coachRequest.getExpertise());
            coach.setPhoneNumber(coachRequest.getPhoneNumber().trim());
            coach.setDob(coachRequest.getDob());
            coach.setGender(coachRequest.getGender());
            coach.setRole(Role.COACH);
            coach.setStatus(Status.ACTIVE);
            coach.setDateCreated(LocalDate.now());
            coach.setDateUpdated(LocalDate.now());
            coachRepository.save(coach);
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
    public Response loginCoach(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()));
            var coach = coachRepository.findByEmailAndStatus(loginRequest.getEmail(), Status.ACTIVE)
                    .orElseThrow(() -> new OurException("Coach not found"));
            var token = jwtUtils.generateToken(coach);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(coach.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");
            response.setCoach(Converter.convertCoachToDTO(coach));
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
    public Response getAllCoaches() {
        Response response = new Response();
        try {
            List<Coach> coaches = coachRepository.findAll();
            List<CoachDTO> dtos = coaches.stream()
                    .map(Converter::convertCoachToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setCoaches(dtos);
            response.setMessage("Fetched all coaches");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteCoach(Long coachId) {
        Response response = new Response();
        try {
            Coach coach = coachRepository.findById(coachId)
                    .orElseThrow(() -> new OurException("Coach not found"));
            coach.setStatus(Status.DELETED);
            coachRepository.save(coach);

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
    public Response getCoachById(Long coachId) {
        Response response = new Response();
        try {
            Coach coach = coachRepository.findById(coachId)
                    .orElseThrow(() -> new OurException("Coach not found"));
            response.setStatusCode(200);
            response.setCoach(Converter.convertCoachToDTO(coach));
            response.setMessage("Fetched coach by id");
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
    public Response updateCoach(Long coachId, CoachDTO coachDTO) {
        Response response = new Response();
        try {
            Coach coach = coachRepository.findById(coachId)
                    .orElseThrow(() -> new OurException("Coach not found"));
            if (coachDTO.getEmail() != null && !coachDTO.getEmail().trim().equals(coach.getEmail())) {
                coach.setEmail(coachDTO.getEmail().trim());
            }
            if (coachDTO.getUsername() != null && !coachDTO.getUsername().trim().equals(coach.getUsername())) {
                coach.setUsername(coachDTO.getUsername().trim());
            }
            if (coachDTO.getName() != null) coach.setName(coachDTO.getName().trim());
            if (coachDTO.getPassword() != null) coach.setPassword(passwordEncoder.encode(coachDTO.getPassword().trim()));
            if (coachDTO.getExpertise() != null) coach.setExpertise(coachDTO.getExpertise());
            if (coachDTO.getPhoneNumber() != null && !coachDTO.getPhoneNumber().trim().isEmpty()) {
                if (coachDTO.getPhoneNumber().trim().length() < 10 || coachDTO.getPhoneNumber().trim().length() > 15) {
                    throw new OurException("Phone number must be between 10 and 15 characters");
                }
                coach.setPhoneNumber(coachDTO.getPhoneNumber().trim());
            }
            if (coachDTO.getDob() != null) {
                if (coachDTO.getDob().isAfter(java.time.LocalDate.now())) {
                    throw new OurException("Date of birth cannot be in the future");
                }
                coach.setDob(coachDTO.getDob());
            }
            if (coachDTO.getGender() != null) coach.setGender(coachDTO.getGender());
            coach.setDateUpdated(LocalDate.now());
            coachRepository.save(coach);
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

