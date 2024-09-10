package com.snowflake_guide.tourmate.global.auth.email.service;

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

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

    // 인증코드 이메일 발송
    public ResponseEntity<Map<String, String>> verifyEmail(String email, String type) {
        // 이메일 메시지 객체 생성
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("[midpoint] 이메일 인증을 위한 인증 코드 발송")
                .build();

        // 이메일 인증을 위한 인증 코드 생성
        String code = generateVerificationCode();

        // 새로운 인증 코드를 추가하며 기존 인증 코드를 무효화
        verificationCodes.put(emailMessage.getTo(), new VerificationCode(code));

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            // 실제 이메일 인증 코드 발송
            sendCodeToSignUpMemberEmail(type, mimeMessage, emailMessage, code);
            log.info("인증코드가 성공적으로 전송되었습니다. 이메일: {}", emailMessage.getTo());
            return ResponseEntity.ok(Map.of("message", "인증 코드가 이메일로 발송되었습니다. 이전 창으로 돌아가 4분 이내에 입력을 완료해주세요."));
        } catch (MessagingException e) {
            log.error("인증코드 전송 실패했습니다. 이메일: {}", emailMessage.getTo(), e);
            throw new RuntimeException("인증코드 전송 실패했습니다.", e);
        }
    }

    private void sendCodeToSignUpMemberEmail(String type, MimeMessage mimeMessage, EmailMessage emailMessage, String code) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(emailMessage.getTo()); // 받는 사람
        mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
        mimeMessageHelper.setText(setContext(code, type), true); // 메일 본문: setText(setContext(인증코드, html파일명), HTML 여부)
        javaMailSender.send(mimeMessage);
    }

    // 영문자, 숫자, 특수문자를 포함한 6자리 인증 코드 생성
    private String generateVerificationCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(chars.length());
            code.append(chars.charAt(randomIndex));
        }
        return code.toString();
    }

    // 메일 형식 생성
    // thymeleaf를 통해 html 적용
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

    // 인증코드 검증 및 인증 시간 기록
    public ResponseEntity<Map<String, String>> verifyCode(String email, String code) {
        VerificationCode verificationCode = verificationCodes.get(email);
        if (verificationCode == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid_code", "message", "유효하지 않은 인증번호입니다."));
        }
        // 사용자가 입력한 값과 실제 인증코드가 일치하는지 확인
        boolean isCorrect = verificationCode.code().equals(code);
        if (isCorrect) {
            verifiedEmails.put(email, true);
            return ResponseEntity.ok(Map.of("message", "올바른 인증번호입니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid_code", "message", "유효하지 않은 인증번호입니다."));
        }
    }

    private record VerificationCode(String code) {
    }
}