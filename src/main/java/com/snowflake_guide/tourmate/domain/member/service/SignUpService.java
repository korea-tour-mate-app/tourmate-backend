package com.snowflake_guide.tourmate.domain.member.service;

import com.snowflake_guide.tourmate.domain.member.dto.SignUpRequestDTO;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignUpService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member signUp(SignUpRequestDTO signUpRequestDTO) {
        Member member = new Member();
        member.setName(signUpRequestDTO.getUsername());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());
        member.setPassword(encodedPassword);

        member.setEmail(signUpRequestDTO.getEmail());

        return memberRepository.save(member);
    }

    public boolean existsByUsername(String username) {
        return memberRepository.existsByName(username);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
