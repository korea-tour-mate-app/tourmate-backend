package com.snowflake_guide.tourmate.domain.restaurant.api;

import com.snowflake_guide.tourmate.domain.restaurant.dto.FindRestaurantDetailsResponseDto;
import com.snowflake_guide.tourmate.domain.restaurant.service.FindRestaurantDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Find Restaurant Review API", description = "음식점 세부정보 및 리뷰 반환")

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class FindRestaurantDetailsController {
    private final FindRestaurantDetailsService restaurantService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<FindRestaurantDetailsResponseDto> getRestaurantDetails(
            @PathVariable Long restaurantId) {
        FindRestaurantDetailsResponseDto responseDto = restaurantService.getRestaurantDetails(restaurantId);
        return ResponseEntity.ok(responseDto);
    }
}
