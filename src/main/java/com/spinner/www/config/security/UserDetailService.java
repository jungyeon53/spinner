package com.spinner.www.config.security;

import com.spinner.www.member.entity.Member;
import com.spinner.www.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberService.getMember(email);

        if(member == null) {
            throw new NoSuchElementException("File not found with email");
        }

        return User.builder()
                .username(member.getMemberEmail())
                .password(member.getMemberPassword())
                .roles("ROLE_USER")
                .build();
    }
}
