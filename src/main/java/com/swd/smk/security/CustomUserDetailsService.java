package com.swd.smk.security;

import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.repository.AdminRepository;
import com.swd.smk.repository.CoachRepository;
import com.swd.smk.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Admin đăng nhập bằng username
        if (adminRepository.findByUsernameAndStatus(usernameOrEmail, Status.ACTIVE).isPresent()) {
            return adminRepository.findByUsernameAndStatus(usernameOrEmail, Status.ACTIVE)
                    .orElseThrow(() -> new UsernameNotFoundException("No admin found with username: " + usernameOrEmail));
        }
        // Member đăng nhập bằng email
        if (memberRepository.findByEmailAndStatus(usernameOrEmail, Status.ACTIVE).isPresent()) {
            return memberRepository.findByEmailAndStatus(usernameOrEmail, Status.ACTIVE)
                    .orElseThrow(() -> new UsernameNotFoundException("No member found with email: " + usernameOrEmail));
        }
        // Coach đăng nhập bằng email
        if (coachRepository.findByEmailAndStatus(usernameOrEmail, Status.ACTIVE).isPresent()) {
            return coachRepository.findByEmailAndStatus(usernameOrEmail, Status.ACTIVE)
                    .orElseThrow(() -> new UsernameNotFoundException("No coach found with email: " + usernameOrEmail));
        }
        throw new UsernameNotFoundException("No user found with username/email: " + usernameOrEmail);
    }
}
