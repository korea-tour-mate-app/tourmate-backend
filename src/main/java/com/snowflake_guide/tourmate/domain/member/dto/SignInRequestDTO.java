package com.snowflake_guide.tourmate.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {
    private String email;
    private String password;
}
