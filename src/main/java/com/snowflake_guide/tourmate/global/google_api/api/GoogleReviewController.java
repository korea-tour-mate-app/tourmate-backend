package com.snowflake_guide.tourmate.global.google_api.api;

import com.snowflake_guide.tourmate.global.google_api.dto.GoogleReviewRequestDto;
import com.snowflake_guide.tourmate.global.google_api.dto.GoogleReviewResponseDto;
import com.snowflake_guide.tourmate.global.google_api.service.GoogleReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Google Restaurant Review API", description = "별점이 높은 음식점 리뷰를 반환해주는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-api")

public class GoogleReviewController {
    private final GoogleReviewService googleReviewService;
    @PostMapping("/optimize-route/driving")
    public GoogleReviewResponseDto optimizeRoute(@RequestBody GoogleReviewRequestDto requestDto) {
        return googleReviewService.getAllReviews(requestDto);
    }
}
