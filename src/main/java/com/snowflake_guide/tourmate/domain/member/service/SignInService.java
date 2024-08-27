package com.snowflake_guide.tourmate.domain.member.service;

import com.snowflake_guide.tourmate.domain.member.dto.SignInRequestDTO;
import com.snowflake_guide.tourmate.domain.member.dto.SignInResponseDTO;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.global.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SignInService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public SignInResponseDTO login(SignInRequestDTO signInRequestDTO) {
        Member member = memberRepository.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다."));

        if (!passwordEncoder.matches(signInRequestDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());


        return new SignInResponseDTO(accessToken, refreshToken);
    }
}
