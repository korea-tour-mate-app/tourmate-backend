package com.snowflake_guide.tourmate.domain.restaurant.service;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlacesAPIResponseDto;
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
    public void saveAllRestaurants(List<GooglePlacesAPIResponseDto.Result> results) {
        List<Restaurant> restaurants = new ArrayList<>();

        for (GooglePlacesAPIResponseDto.Result result : results) {
            Restaurant restaurant = Restaurant.builder()
                    .name(result.getName())
                    .formattedAddress(result.getFormatted_address())
                    .latitude(result.getGeometry().getLocation().getLat())
                    .longitude(result.getGeometry().getLocation().getLng())
                    .openNow(result.getOpening_hours() != null && result.getOpening_hours().isOpen_now())
                    .placeId(result.getPlace_id())
                    .priceLevel(result.getPrice_level())
                    .reference(result.getReference())
                    .userRatingsTotal(result.getUser_ratings_total())
                    .build();

            restaurants.add(restaurant); // 리스트에 레스토랑 추가
        }

        restaurantRepository.saveAll(restaurants); // 리스트로 저장
    }
}
