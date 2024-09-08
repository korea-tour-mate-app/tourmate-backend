package com.snowflake_guide.tourmate.domain.member.dto;

import lombok.Getter;

@Getter
public class GoogleMemberResponse {
    private String email; // 사용자의 이메일
    private String name; // 사용자의 이름
}
