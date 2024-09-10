package com.snowflake_guide.tourmate.global.auth.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 회원가입 시 유효한 이메일인지 판단하기 위해 입력받는다.
 */
@Getter
public class VerifyEmailRequest {
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;
}
