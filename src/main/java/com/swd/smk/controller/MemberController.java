package com.swd.smk.controller;

import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.MemberDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.services.impl.MemberService;
import com.swd.smk.services.interfac.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @PostMapping("/public/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = memberService.loginMember(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/public/register")
    public ResponseEntity<Response> createUser(@RequestBody MemberDTO request){
        Response response = memberService.registerMember(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-all-users")
    public ResponseEntity<Response> getAllUsers(){
        Response response = memberService.getAllMembers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-member-by-id/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id){
        Response response = memberService.getMemberById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-member/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        Response response = memberService.deleteMember(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/member/update-member/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody MemberDTO request) {
        Response response = memberService.updateMember(id, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
