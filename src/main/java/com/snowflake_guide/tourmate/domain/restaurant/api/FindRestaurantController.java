package com.snowflake_guide.tourmate.domain.restaurant.api;

import com.snowflake_guide.tourmate.domain.restaurant.service.FindRestaurantService;
import com.snowflake_guide.tourmate.domain.restaurant_review.dto.FindRestaurantsRequestDto;
import com.snowflake_guide.tourmate.domain.restaurant_review.dto.FindRestaurantsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class FindRestaurantController {

    private final FindRestaurantService findRestaurantService;

    @PostMapping("/list")
    public ResponseEntity<FindRestaurantsResponseDto> getRestaurantPlaceId(@RequestBody FindRestaurantsRequestDto findRestaurantsRequestDto) {
        FindRestaurantsResponseDto responseDto = findRestaurantService.getRestaurantPlaceId(findRestaurantsRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
