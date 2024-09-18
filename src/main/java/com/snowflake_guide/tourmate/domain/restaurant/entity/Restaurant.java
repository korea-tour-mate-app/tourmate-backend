package com.snowflake_guide.tourmate.domain.restaurant.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;

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
    private String formattedAddress; // 주소
    private double latitude; // 위도
    private double longitude; // 경도
    private String placeId; // 장소 ID
    private int priceLevel; // 가격 수준
    private String reference; // 참조값
    private double rating; // 리뷰값
    private int userRatingsTotal; // 리뷰 수
    private String formattedPhoneNumber; // 전화번호
    private String weekdayText; // 요일별 영업시간 텍스트

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public void setWeekdayText(String weekdayText) {
        this.weekdayText = weekdayText;
    }

    // 리뷰와의 1:N 관계 설정
//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RestaurantReview> reviews = new ArrayList<>();
}
