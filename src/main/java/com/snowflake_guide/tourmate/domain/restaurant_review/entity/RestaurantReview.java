package com.snowflake_guide.tourmate.domain.restaurant_review.entity;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String authorName; // 작성자 이름
    private String language; // 언어
    private double rating; // 별점
    private String relativeTimeDescription; // 상대적 시간

    @Column(columnDefinition = "TEXT") // 텍스트 필드를 TEXT 타입으로 설정
    private String text; // 리뷰 내용
    private long time; // 리뷰 작성 시간 (유닉스 타임스탬프)

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant; // Restaurant와의 관계 설정
}
