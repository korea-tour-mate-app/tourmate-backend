package com.snowflake_guide.tourmate.global.google_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RestaurantReviewResponseDto {
    private String formatted_phone_number; // 전화번호
    private List<String> weekday_text; // 요일별 영업시간 텍스트
    private List<Review> reviews = new ArrayList<>(); // ArrayList로 초기화

    @Override
    public String toString() {
        return "RestaurantReviewResponseDto{" +
                "formatted_phone_number='" + formatted_phone_number + '\'' +
                ", weekday_text=" + weekday_text +
                ", reviews=" + reviews +
                '}';
    }
    @Getter
    @Setter
    public static class Review {
        private String author_name; // 작성자 이름
        private String language; // 언어
        private double rating; // 별점
        private String relative_time_description; // 상대적 시간
        private String text; // 리뷰 내용
        private long time; // 리뷰 작성 시간 (유닉스 타임스탬프)

        @Override
        public String toString() {
            return "Review{" +
                    "author_name='" + author_name + '\'' +
                    ", language='" + language + '\'' +
                    ", rating=" + rating +
                    ", relative_time_description='" + relative_time_description + '\'' +
                    ", text='" + text + '\'' +
                    ", time=" + time +
                    '}';
        }
    }
}