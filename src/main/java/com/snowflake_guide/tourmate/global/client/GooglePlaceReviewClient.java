package com.snowflake_guide.tourmate.global.client;

import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlaceDetailsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googlePlaceReviewClient", url = "https://maps.googleapis.com/maps/api/place")
public interface GooglePlaceReviewClient {

    @GetMapping("/details/json")
    GooglePlaceDetailsResponseDto getPlaceReviews(
            @RequestParam("place_id") String placeId,
            @RequestParam("key") String apiKey,
            @RequestParam("language") String language
    );
}
