package com.snowflake_guide.tourmate.global.google_api.api;

import com.snowflake_guide.tourmate.global.google_api.service.GooglePlaceIdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[초기세팅] Google Place ID API", description = "서울 내 음식점 PlaceId를 반환해주는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-api")
public class GooglePlaceIdController {
    private final GooglePlaceIdService googlePlaceIdService;

    @PostMapping("/getTopRestaurantPlaces")
    public ResponseEntity<?> getPlaceIds(@RequestParam(value = "pageToken", required = false) String pageToken) {
        return googlePlaceIdService.getAllRestaurantPlaces();
    }
}