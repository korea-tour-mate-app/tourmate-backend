package com.snowflake_guide.tourmate.global.google_api.api;

import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantReviewResponseDto;
import com.snowflake_guide.tourmate.global.google_api.service.GoogleDetailsService;
import com.snowflake_guide.tourmate.global.google_api.service.GooglePlaceIdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Google Place Details API", description = "PlaceId별 리뷰 내역을 반환해주는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-api")
public class GooglePlaceDetailsController {
    private final GoogleDetailsService googleDetailsService;

    @PostMapping("/getRestaurantReviews")
    public ResponseEntity<RestaurantReviewResponseDto> getRestaurantReviews(@RequestParam String placeId) {
        // 서비스 호출 후 ResponseEntity로 반환
        RestaurantReviewResponseDto responseDto = googleDetailsService.getRestaurantReviews(placeId);
        return ResponseEntity.ok(responseDto);
    }
}