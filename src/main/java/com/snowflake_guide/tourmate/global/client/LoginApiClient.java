package com.snowflake_guide.tourmate.global.client;

import com.snowflake_guide.tourmate.domain.member.dto.GoogleMemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "googleLoginApiClient", url = "https://openidconnect.googleapis.com")
public interface LoginApiClient {
    @GetMapping("/v1/userinfo")
    GoogleMemberResponse getGoogleMemberInfo(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}