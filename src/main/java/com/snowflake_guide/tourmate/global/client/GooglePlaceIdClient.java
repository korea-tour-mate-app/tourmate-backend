package com.snowflake_guide.tourmate.global.client;

import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlacesAPIResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googlePlaceClient", url = "https://maps.googleapis.com/maps/api/place")
public interface GooglePlaceIdClient {
    @GetMapping("/textsearch/json")
    GooglePlacesAPIResponseDto getTopRestaurants(
            @RequestParam("query") String query,
            @RequestParam("type") String type,
            @RequestParam("rankby") String rankBy,
            @RequestParam("key") String apiKey,
            @RequestParam("language") String language
    );
}
