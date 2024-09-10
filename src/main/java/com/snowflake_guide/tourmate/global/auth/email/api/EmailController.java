package com.snowflake_guide.tourmate.global.auth.email.api;

import com.snowflake_guide.tourmate.global.auth.email.dto.VerifyCodeRequest;
import com.snowflake_guide.tourmate.global.auth.email.dto.VerifyEmailRequest;
import com.snowflake_guide.tourmate.global.auth.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 이메일 관련 인증 API 컨트롤러 클래스
 * 이메일 인증, 인증 코드 확인 등의 기능을 제공한다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class EmailController {
    private final EmailService emailService;

    /**
     * 회원가입 시 이메일 인증을 위해 인증 코드를 발송한다.
     *
     * @param verifyEmailRequest 이메일 인증 요청 DTO
     * @return 인증 코드 발송 결과 응답
     */
    @PostMapping("/verify-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody @Valid VerifyEmailRequest verifyEmailRequest) {
        // 인증 이메일 발송
        return emailService.verifyEmail(verifyEmailRequest.getEmail(), "verify");
    }

    /**
     * 회원가입 시 사용자가 입력한 인증 코드를 검증한다.
     *
     * @param verifyCodeRequest 인증 코드 검증 요청 DTO
     * @return 인증 코드 검증 결과 응답
     */
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody @Valid VerifyCodeRequest verifyCodeRequest) {
        // 인증 코드 검증
        return emailService.verifyCode(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
    }
}
