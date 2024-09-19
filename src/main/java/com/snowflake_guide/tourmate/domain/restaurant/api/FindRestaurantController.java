package com.snowflake_guide.tourmate.domain.restaurant.api;

import com.snowflake_guide.tourmate.domain.restaurant.service.FindRestaurantService;
import com.snowflake_guide.tourmate.domain.restaurant_review.dto.FindRestaurantsRequestDto;
import com.snowflake_guide.tourmate.domain.restaurant_review.dto.FindRestaurantsResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Find Restaurant PlaceId API", description = "사용자 장소 위치 근처에 있는 음식점 리스트 반환")
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class FindRestaurantController {

    private final FindRestaurantService findRestaurantService;

    @PostMapping("/list")
    public ResponseEntity<FindRestaurantsResponseDto> getRestaurantPlaceId(@RequestBody FindRestaurantsRequestDto findRestaurantsRequestDto) {
        FindRestaurantsResponseDto responseDto = findRestaurantService.findRestaurantPlaceId(findRestaurantsRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
