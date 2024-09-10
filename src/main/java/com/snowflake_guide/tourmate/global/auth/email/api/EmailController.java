package com.snowflake_guide.tourmate.global.auth.email.api;

import com.snowflake_guide.tourmate.global.auth.email.dto.VerifyCodeRequest;
import com.snowflake_guide.tourmate.global.auth.email.dto.VerifyEmailRequest;
import com.snowflake_guide.tourmate.global.auth.email.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 이메일 관련 인증 API 컨트롤러 클래스
 * 이메일 인증 및 인증 코드 검증을 처리하는 API를 제공합니다.
 */
@Tag(name = "Email Verification API", description = "이메일 인증코드 발송 및 검증 기능을 제공합니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class EmailController {

    private final EmailService emailService;

    /**
     * 회원가입을 위한 이메일 인증 코드를 발송합니다.
     *
     * @param verifyEmailRequest 이메일 인증 요청 데이터
     * @return 인증 코드 발송 결과 응답
     */
    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, String>> sendVerificationEmail(
            @RequestBody @Valid VerifyEmailRequest verifyEmailRequest) {
        log.info("인증 이메일 발송 요청: {}", verifyEmailRequest.getEmail());
        return emailService.verifyEmail(verifyEmailRequest.getEmail(), "verify");
    }

    /**
     * 사용자가 입력한 이메일 인증 코드를 검증합니다.
     *
     * @param verifyCodeRequest 인증 코드 검증 요청 데이터
     * @return 인증 코드 검증 결과 응답
     */
    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, String>> verifyCode(
            @RequestBody @Valid VerifyCodeRequest verifyCodeRequest) {
        log.info("인증 코드 검증 요청: 이메일={}, 코드={}", verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
        return emailService.verifyCode(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
    }
}