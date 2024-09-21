package com.snowflake_guide.tourmate.domain.member.service;

import com.snowflake_guide.tourmate.domain.member.dto.SignUpRequestDTO;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.global.auth.email.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public Member signUp(SignUpRequestDTO signUpRequestDTO) {
        // 이메일 인증 여부 확인
        if (!emailService.isEmailVerified(signUpRequestDTO.getEmail())) {
            throw new RuntimeException("이메일 인증을 먼저 시도해주세요.");
        }

        validatePassword(signUpRequestDTO.getPassword());

        Member member = new Member();
        member.setNickname(signUpRequestDTO.getNickname());

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
    public boolean existsByNickname(String email) {
        return memberRepository.existsByNickname(email);
    }
}
