package com.snowflake_guide.tourmate.global.auth.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 이메일 검증을 위한 dto
 */
@Getter
public class VerifyCodeRequest {
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "코드는 필수 입력 항목입니다.")
    private String code;
}
