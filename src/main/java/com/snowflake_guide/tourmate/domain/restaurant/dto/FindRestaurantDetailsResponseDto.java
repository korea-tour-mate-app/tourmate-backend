package com.snowflake_guide.tourmate.domain.restaurant.dto;

import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantReviewResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class FindRestaurantDetailsResponseDto {
    private int priceLevel; // 가격 수준
    private double totalRating; // 총 리뷰값
    private int userRatingsTotal; // 리뷰 수
    private String formattedPhoneNumber; // 전화번호
    private String weekdayText; // 요일별 영업시간 텍스트
    private List<Review> reviews; // 리뷰 정보 리스트

    @Getter
    @Builder
    public static class Review {
        private String author_name; // 작성자 이름
        private String language; // 언어
        private double rating; // 별점
        private String relative_time_description; // 상대적 시간
        private String text; // 리뷰 내용
        private long time; // 리뷰 작성 시간 (유닉스 타임스탬프)
    }
}
