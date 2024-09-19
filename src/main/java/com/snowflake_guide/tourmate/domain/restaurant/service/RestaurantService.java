package com.snowflake_guide.tourmate.domain.restaurant.service;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantResponseDto;
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
    public void saveAllRestaurants(RestaurantResponseDto restaurantResponseDto) {
        List<RestaurantResponseDto.PlaceDetailResult> list = restaurantResponseDto.getPlaceDetailResults();
        String nextPageToken = restaurantResponseDto.getNext_page_token();
        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            RestaurantResponseDto.PlaceDetailResult result = list.get(i);

            // 첫 번째 PlaceDetailResult인 경우에만 nextPageToken 설정
            Restaurant.RestaurantBuilder builder = Restaurant.builder()
                    .name(result.getName())
                    .formattedAddress(result.getFormattedAddress())
                    .latitude(result.getLatitude())
                    .longitude(result.getLongitude())
                    .placeId(result.getPlaceId())
                    .priceLevel(result.getPriceLevel())
                    .reference(result.getReference())
                    .userRatingsTotal(result.getUserRatingsTotal())
                    .rating(result.getRating());

            if (i == 0) { // 첫 번째 요소인 경우 nextPageToken 추가
                builder.nextPageToken(nextPageToken);
            }
            Restaurant restaurant = builder.build();
            restaurants.add(restaurant); // 리스트에 레스토랑 추가
        }

        // Restaurant 엔티티 리스트를 한꺼번에 저장
        restaurantRepository.saveAll(restaurants);
    }
}
