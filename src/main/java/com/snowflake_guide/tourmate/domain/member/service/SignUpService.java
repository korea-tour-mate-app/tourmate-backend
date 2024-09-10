package com.snowflake_guide.tourmate.domain.member.service;

import com.snowflake_guide.tourmate.domain.member.dto.SignUpRequestDTO;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        validatePassword(signUpRequestDTO.getPassword());

        Member member = new Member();
        member.setNickname(signUpRequestDTO.getName());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());
        member.setPassword(encodedPassword);

        member.setEmail(signUpRequestDTO.getEmail());

        return memberRepository.save(member);
    }

    private void validatePassword(String password) {
        if (!StringUtils.hasText(password) || password.length() < 6) {
            throw new IllegalArgumentException("비밀번호는 최소 6자리 이상이어야 합니다.");
        }
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
