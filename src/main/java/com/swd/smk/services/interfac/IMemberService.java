package com.swd.smk.services.interfac;

import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.MemberDTO;
import com.swd.smk.dto.Response;

public interface IMemberService {

    Response registerMember(MemberDTO memberRequest);

    Response loginMember(LoginRequest loginRequest);

    Response getAllMembers();

    Response deleteMember(Long memberId);

    Response getMemberById(Long memberId);

    Response updateMember(Long memberId, MemberDTO memberDTO);
}
