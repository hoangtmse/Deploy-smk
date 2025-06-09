package com.swd.smk.services.impl;

import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.MemberDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Role;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.services.interfac.IMemberService;
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
public class MemberService implements IMemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response registerMember(MemberDTO memberRequest) {
        Response response = new Response();
        try {

            if(memberRepository.findByEmailAndStatus(memberRequest.getEmail().trim(), Status.ACTIVE).isPresent()) {
                throw new OurException("Email already existed");
            }
            if(memberRepository.findByUsernameAndStatus(memberRequest.getUsername().trim(), Status.ACTIVE). isPresent()){
                throw new OurException("Username already existed");
            }
            if (memberRequest.getDob() == null) {
                throw new OurException("Date of birth is required");
            }
            if (memberRequest.getDob().isAfter(LocalDate.now())) {
                throw new OurException("Date of birth cannot be in the future");
            }
            if (memberRequest.getPhoneNumber() == null || memberRequest.getPhoneNumber().trim().isEmpty()) {
                throw new OurException("Phone number is required");
            }
            if (memberRequest.getPhoneNumber().trim().length() < 10 || memberRequest.getPhoneNumber().trim().length() > 15) {
                throw new OurException("Phone number must be between 10 and 15 characters");
            }
            String encodedPassword = passwordEncoder.encode(memberRequest.getPassword().trim());

            Member member = new Member();
            member.setEmail(memberRequest.getEmail().trim());
            member.setUsername(memberRequest.getUsername().trim());
            member.setFullName(memberRequest.getFullName().trim());
            member.setPassword(encodedPassword);
            member.setPhoneNumber(memberRequest.getPhoneNumber().trim());
            member.setRole(Role.MEMBER);
            member.setStatus(Status.ACTIVE);
            member.setDateCreated(LocalDate.now());
            member.setDateUpdated(LocalDate.now());
            member.setJoinDate(LocalDate.now());
            member.setDob(memberRequest.getDob());
            member.setGender(memberRequest.getGender());
            memberRepository.save(member);

            response.setStatusCode(200);
            response.setMessage("Registered successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            System.out.println("DOB: " + memberRequest.getDob());
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response loginMember(LoginRequest loginRequest) {

        Response response = new Response();

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()));
            var member = memberRepository.findByEmailAndStatus(loginRequest.getEmail(), Status.ACTIVE)
                    .orElseThrow(() -> new OurException("user Not found"));
            var token = jwtUtils.generateToken(member);

            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(member.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllMembers() {
        Response response = new Response();
        try {
            List<Member> members = memberRepository.findAll();
            List<MemberDTO> dtos = members.stream()
                    .map(Converter::convertMemberToDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMembers(dtos);
            response.setMessage("Fetched all members");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteMember(Long memberId) {
        Response response = new Response();
        try{
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new OurException("Not found: " + memberId));
            member.setStatus(Status.DELETED);
            memberRepository.save(member);
            response.setStatusCode(200);
            response.setMessage("Deleted successfully");
        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMemberById(Long memberId) {
        Response response = new Response();
        try{
            Member member = memberRepository.findByIdAndStatus(memberId, Status.ACTIVE)
                    .orElseThrow(() -> new OurException("Not found: " + memberId));
            MemberDTO dto = new MemberDTO();
            dto = Converter.convertMemberToDTO(member);
            response.setMember(dto);
            response.setStatusCode(200);
            response.setMessage("Deleted successfully");
        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateMember(Long memberId, MemberDTO memberDTO) {
        Response response = new Response();
        try {
            Member member = memberRepository.findByIdAndStatus(memberId, Status.ACTIVE)
                    .orElseThrow(() -> new OurException("Not found: " + memberId));

            if (memberDTO.getEmail() != null && !memberDTO.getEmail().trim().equals(member.getEmail())) {
                if (memberRepository.findByEmailAndStatus(memberDTO.getEmail().trim(), Status.ACTIVE).isPresent()) {
                    throw new OurException("Email already existed");
                }
                member.setEmail(memberDTO.getEmail().trim());
            }
            if (memberDTO.getUsername() != null && !memberDTO.getUsername().trim().equals(member.getUsername())) {
                if (memberRepository.findByUsernameAndStatus(memberDTO.getUsername().trim(), Status.ACTIVE).isPresent()) {
                    throw new OurException("Username already existed");
                }
                member.setUsername(memberDTO.getUsername().trim());
            }
            if (memberDTO.getFullName() != null) {
                member.setFullName(memberDTO.getFullName().trim());

            }
            if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
                member.setPassword(passwordEncoder.encode(memberDTO.getPassword().trim()));

            }
            if (memberDTO.getPhoneNumber() != null) {
                member.setPhoneNumber(memberDTO.getPhoneNumber().trim());
                if (member.getPhoneNumber().length() < 10 || member.getPhoneNumber().length() > 15) {
                    throw new OurException("Phone number must be between 10 and 15 characters");
                }
            }
            if (memberDTO.getDob() != null) {
                member.setDob(memberDTO.getDob());
                if (member.getDob().isAfter(LocalDate.now())) {
                    throw new OurException("Date of birth cannot be in the future");
                }
            }
            if (memberDTO.getGender() != null) {
                member.setGender(memberDTO.getGender());
            }
            member.setDateUpdated(LocalDate.now());

            // Không cho phép cập nhật role và status qua API này
            memberRepository.save(member);
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
