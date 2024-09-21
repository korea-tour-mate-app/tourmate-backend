package com.snowflake_guide.tourmate.global.auth.email.dto;

import lombok.Builder;
import lombok.Getter;


/**
 * 이메일 전송 객체 생성
 */
@Getter
@Builder
public class EmailMessage {
    private String to; // 받는 사람
    private String subject; // 메일 제목
}
