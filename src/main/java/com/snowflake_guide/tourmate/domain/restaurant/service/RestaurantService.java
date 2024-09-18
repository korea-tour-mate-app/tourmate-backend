package com.snowflake_guide.tourmate.domain.restaurant.service;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.global.google_api.dto.GoogleRestaurantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void saveAllRestaurants(List<GoogleRestaurantResponseDto.PlaceDetailResult> list) {
        List<Restaurant> restaurants = new ArrayList<>();

        for (GoogleRestaurantResponseDto.PlaceDetailResult result : list) {
            Restaurant restaurant = Restaurant.builder()
                    .name(result.getName())
                    .formattedAddress(result.getFormattedAddress())
                    .latitude(result.getLatitude())
                    .longitude(result.getLongitude())
                    .placeId(result.getPlaceId())
                    .priceLevel(result.getPriceLevel())
                    .reference(result.getReference())
                    .userRatingsTotal(result.getUserRatingsTotal())
                    .rating(result.getRating())
                    .build();

            restaurants.add(restaurant); // 리스트에 레스토랑 추가
        }

        // Restaurant 엔티티 리스트를 한꺼번에 저장
        restaurantRepository.saveAll(restaurants);
    }
}
