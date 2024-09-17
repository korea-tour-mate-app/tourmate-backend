package com.snowflake_guide.tourmate.global.client;

import com.snowflake_guide.tourmate.domain.member.dto.GoogleMemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "googleApiClient", url = "")
public interface GoogleReviewClient {
    // 음식점 리스트 받아오기
    @GetMapping("")
    GoogleMemberResponse getPlaceSearchInfo();

    // 음식점 리뷰 가져오기
    @GetMapping("")
    GoogleMemberResponse getPlaceDetailsInfo();
}
