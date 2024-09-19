package com.snowflake_guide.tourmate.domain.restaurant.dto;


import lombok.*;

@Getter
public class FindRestaurantDetailsRequestDto {
    private String authorName; // 작성자 이름
    private String language; // 언어
    private double reviewRating; // 별점
    private String relativeTimeDescription; // 상대적 시간
    private String text; // 리뷰 내용
    private long time; // 리뷰 작성 시간 (유닉스 타임스탬프)
}
