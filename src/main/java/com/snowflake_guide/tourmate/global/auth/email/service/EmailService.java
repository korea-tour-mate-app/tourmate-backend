package com.snowflake_guide.tourmate.global.auth.email.service;

import com.snowflake_guide.tourmate.domain.member.service.MemberService;
import com.snowflake_guide.tourmate.global.auth.email.dto.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final int CODE_LENGTH = 6;
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String SUBJECT = "[TOURMATE] Verification code";
    private static final String EMAIL_ALREADY_IN_USE_ERROR = "이미 사용중인 이메일입니다.";
    private static final String INVALID_CODE_ERROR = "유효하지 않은 인증번호입니다.";
    private static final String EMAIL_SENT_MESSAGE = "인증 코드가 이메일로 발송되었습니다.";
    private static final String CORRECT_CODE_MESSAGE = "올바른 인증번호입니다.";

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberService memberService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();

    // 이메일 인증 요청
    public ResponseEntity<Map<String, String>> verifyEmail(String email, String type) {
        // 이메일 중복 체크
        if (memberService.isEmailAlreadyInUse(email)) {
            return createErrorResponse("email_in_use", EMAIL_ALREADY_IN_USE_ERROR);
        }

        String code = generateVerificationCode();
        EmailMessage emailMessage = buildEmailMessage(email, code);

        try {
            sendVerificationCode(type, emailMessage, code);
            log.info("인증코드가 성공적으로 전송되었습니다. 이메일: {}", email);
            return createSuccessResponse(EMAIL_SENT_MESSAGE);
        } catch (MessagingException e) {
            log.error("인증코드 전송 실패: {}", email, e);
            throw new EmailSendingException("인증코드 전송 실패", e);
        }
    }

    private EmailMessage buildEmailMessage(String email, String code) {
        // 새로운 인증 코드를 추가하며 기존 인증 코드를 무효화
        verificationCodes.put(email, new VerificationCode(code));

        return EmailMessage.builder()
                .to(email)
                .subject(SUBJECT)
                .build();
    }

    private void sendVerificationCode(String type, EmailMessage emailMessage, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setTo(emailMessage.getTo());
        mimeMessageHelper.setSubject(emailMessage.getSubject());
        mimeMessageHelper.setText(buildEmailContent(code, type), true);

        javaMailSender.send(mimeMessage);
    }

    private String buildEmailContent(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

    private String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(CHAR_POOL.length());
            code.append(CHAR_POOL.charAt(randomIndex));
        }
        return code.toString();
    }

    // 인증코드 검증
    public ResponseEntity<Map<String, String>> verifyCode(String email, String inputCode) {
        VerificationCode storedCode = verificationCodes.get(email);

        if (storedCode == null || !storedCode.code().equals(inputCode)) {
            return createErrorResponse("invalid_code", INVALID_CODE_ERROR);
        }

        return createSuccessResponse(CORRECT_CODE_MESSAGE);
    }

    // 유틸리티 메서드: 에러 응답 생성
    private ResponseEntity<Map<String, String>> createErrorResponse(String error, String message) {
        return ResponseEntity.badRequest().body(Map.of("error", error, "message", message));
    }

    // 유틸리티 메서드: 성공 응답 생성
    private ResponseEntity<Map<String, String>> createSuccessResponse(String message) {
        return ResponseEntity.ok(Map.of("message", message));
    }

    // 커스텀 예외 클래스
    public static class EmailSendingException extends RuntimeException {
        public EmailSendingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // 인증코드 기록용 클래스 (Java 16의 record 사용)
    private record VerificationCode(String code) {
    }

    // 이미 인증을 마친 이메일인지 확인
    public boolean isEmailVerified(String email) {
        VerificationCode storedCode = verificationCodes.get(email);
        // 인증 코드가 없거나, 확인된 상태라면 인증이 완료된 것으로 간주
        return storedCode != null && storedCode.code().isEmpty();
    }
}