package com.snowflake_guide.tourmate.domain.restaurant_review.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindRestaurantsResponseDto {

    private List<LocationWithRestaurants> locationsWithRestaurants; // 위치와 해당 위치의 음식점 목록

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationWithRestaurants {
        private FindRestaurantsRequestDto.LocationRequestDto place_location; // 위치 정보
        private List<RestaurantInfo> restaurants; // 해당 위치의 음식점 목록
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RestaurantInfo {
        private Long restaurantId; // 레스토랑 ID
        private String res_name; // 장소 이름
        private double res_latitude; // 위도
        private double res_longitude; // 경도
    }
}

