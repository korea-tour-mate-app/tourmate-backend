package com.snowflake_guide.tourmate.domain.restaurant.entity;

import com.snowflake_guide.tourmate.domain.restaurant_review.entity.RestaurantReview;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    private String name; // 장소 이름

    @Column(length = 1000)
    private String formattedAddress; // 주소
    private double latitude; // 위도
    private double longitude; // 경도

    @Column(length = 1000)
    private String placeId; // 장소 ID
    private int priceLevel; // 가격 수준

    @Column(length = 1000)
    private String reference; // 참조값

    @Column(columnDefinition = "double precision default 0.0") // 기본값 0.0 설정
    private double totalRating; // 리뷰값
    private int userRatingsTotal; // 리뷰 수

//    @Column(length = 3000) // 길이를 늘려서 오류 방지
//    private String nextPageToken; // 장소 요청하는 다음 토큰

    @Column(length = 500)
    private String formattedPhoneNumber; // 전화번호

    @Column(length = 2000)
    private String weekdayText; // 요일별 영업시간 텍스트
    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public void setWeekdayText(String weekdayText) {
        this.weekdayText = weekdayText;
    }

    // 리뷰와의 1:N 관계 설정
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantReview> reviews = new ArrayList<>();
}