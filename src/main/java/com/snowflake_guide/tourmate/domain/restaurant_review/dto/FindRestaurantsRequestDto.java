package com.snowflake_guide.tourmate.domain.restaurant_review.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindRestaurantsRequestDto {
    List<LocationRequestDto> place_locations; // 사용자가 입력한 위치 리스트

    @Getter
    public static class LocationRequestDto {
        private double place_latitude;  // 위도
        private double place_longitude; // 경도
        private double place_order; // 몇 번째 장소인지 판별
    }
}