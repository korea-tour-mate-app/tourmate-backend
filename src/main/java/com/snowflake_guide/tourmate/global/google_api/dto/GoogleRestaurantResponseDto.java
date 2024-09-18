package com.snowflake_guide.tourmate.global.google_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleRestaurantResponseDto {
    List<PlaceDetailResult> placeDetailResults;
    private String next_page_token;

    @Getter
    @Setter
    public static class PlaceDetailResult {
        private String name; // 장소 이름
        private String formattedAddress; // 주소
        private double latitude; // 위도
        private double longitude; // 경도
        private String placeId; // 장소 ID
        private int priceLevel; // 가격 수준
        private String reference; // 참조값
        private int userRatingsTotal; // 리뷰 수
        private double rating; // 별점

        @Override
        public String toString() {
            return "PlaceDetailResult{" +
                    "name='" + name + '\'' +
                    ", formattedAddress='" + formattedAddress + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", placeId='" + placeId + '\'' +
                    ", priceLevel=" + priceLevel +
                    ", reference='" + reference + '\'' +
                    ", userRatingsTotal=" + userRatingsTotal +
                    ", rating=" + rating +
                    '}';
        }
    }
}