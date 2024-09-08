package com.snowflake_guide.tourmate.domain.member.service;

import com.snowflake_guide.tourmate.global.client.LoginApiClient;
import com.snowflake_guide.tourmate.domain.member.dto.GoogleMemberResponse;
import com.snowflake_guide.tourmate.domain.member.dto.SignInResponseDTO;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.global.jwt.JwtTokenProvider;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final LoginApiClient loginApiClient;
    private final MemberRepository memberRepository; // MemberRepository를 사용하여 데이터베이스에서 직접 조회
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    // 구글 로그인 및 회원가입 처리 로직
    public SignInResponseDTO processGoogleLogin(String accessToken) {
        try {
            // 구글 사용자 정보 가져오기
            GoogleMemberResponse googleUser = loginApiClient.getGoogleMemberInfo(accessToken);
            log.info("구글 로그인 성공 이메일: {}", googleUser.getEmail());
            // 이메일로 기존 회원 조회
            Member member = memberRepository.findByEmail(googleUser.getEmail())
                    .orElse(null);

            if (member == null) {
                // 기존 회원이 없으면 회원가입 처리
                log.info("새로운 회원을 생성합니다: {}", googleUser.getEmail());
                member = Member.builder()
                        .email(googleUser.getEmail())
                        .name(googleUser.getName())
                        .password("") // 구글 소셜 로그인 사용자는 비밀번호가 없으므로 빈 값으로 설정
                        .build();
                memberRepository.save(member); // 회원 저장 로직
            }

            // JWT Access Token 및 Refresh Token 발급
            String jwtAccessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
            String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

            // SignInResponseDTO로 반환
            return new SignInResponseDTO(jwtAccessToken, jwtRefreshToken);

        } catch (Exception e) {
            log.error("구글 로그인 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("구글 로그인 처리에 실패했습니다.");
        }
    }
}
