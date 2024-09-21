package com.snowflake_guide.tourmate.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponseDTO {
    private String accessToken;
    private String refreshToken;

    public SignInResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
