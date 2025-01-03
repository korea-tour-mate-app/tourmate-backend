package com.snowflake_guide.tourmate.global.google_api.api;

import com.snowflake_guide.tourmate.global.google_api.service.GoogleDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[초기세팅] Google Place Details API", description = "PlaceId별 리뷰 내역을 반환해주는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-api")
public class GooglePlaceDetailsController {
    private final GoogleDetailsService googleDetailsService;

    @PostMapping("/getRestaurantReviews")
    public ResponseEntity<String> fetchReviewsForAllRestaurants(
            @RequestParam(defaultValue = "1") int startId,
            @RequestParam(defaultValue = "499") int endId) {

        // 배치 서비스 호출
        googleDetailsService.getRestaurantReviews(startId, endId);
        return ResponseEntity.ok("Batch processing started for restaurant IDs from " + startId + " to " + endId);
    }
}