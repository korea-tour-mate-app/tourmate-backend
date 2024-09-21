package com.snowflake_guide.tourmate.domain.member.api;

import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@Tag(name = "Nickname API", description = "회원 닉네임 반환하는 API")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/nickname")
    public ResponseEntity<?> getUserNickname(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 사용자 닉네임 추출
            String email = jwtTokenProvider.getName(token);

            // 데이터베이스에서 사용자 정보 조회
            Optional<Member> memberOptional = memberRepository.findByEmail(email);

            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                return ResponseEntity.ok(member.getNickname()); // 닉네임 반환
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } else {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }
}
