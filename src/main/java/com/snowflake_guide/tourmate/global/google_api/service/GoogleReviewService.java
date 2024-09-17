package com.snowflake_guide.tourmate.global.google_api.service;

import com.snowflake_guide.tourmate.global.client.GoogleReviewClient;
import com.snowflake_guide.tourmate.global.google_api.dto.GoogleReviewRequestDto;
import com.snowflake_guide.tourmate.global.google_api.dto.GoogleReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleReviewService {
    private final GoogleReviewClient googleReviewClient;

    @Value("${google.api.key}")
    private String apiKey;

    public GoogleReviewResponseDto getAllReviews(GoogleReviewRequestDto requestDto) {

    }

    // 반경 몇 km 이내에 있는 음식점 placeId 먼저 추출
    // 음식점 placeId로 리뷰 조회
}
