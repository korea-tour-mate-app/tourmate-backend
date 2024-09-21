package com.snowflake_guide.tourmate.domain.member.api;

import com.snowflake_guide.tourmate.domain.member.dto.SignInResponseDTO;
import com.snowflake_guide.tourmate.domain.member.service.GoogleAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Tag(name = "Google (SingUp) SingIn API", description = "구글 (회원가입 후) 로그인하는 API")
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleAuthService googleAuthService;

    @PostMapping("/google-login")
    public ResponseEntity<SignInResponseDTO> googleLogin(@RequestHeader(name=AUTHORIZATION) String authorizationHeader) {
        // 구글 로그인 처리 및 JWT Access/Refresh Token 발급
        SignInResponseDTO signInResponse = googleAuthService.processGoogleLogin(authorizationHeader);

        // Access Token과 Refresh Token을 클라이언트에게 반환
        return ResponseEntity.ok(signInResponse);
    }
}
