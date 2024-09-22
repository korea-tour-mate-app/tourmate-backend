package com.snowflake_guide.tourmate.domain.place.entity;

import com.snowflake_guide.tourmate.domain.like.entity.Like;
import com.snowflake_guide.tourmate.domain.theme.entity.Theme;
import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "place")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @Column(nullable = false)
    private Float latitude; // 위도

    @Column(nullable = false)
    private Float longitude; // 경도

    @Column(nullable = false)
    private String placeName; // 여행지 이름

    @Column(nullable = false, length = 300)
    private String placeLocation; // 주소

    @Column(length = 300)
    private String imageUrl1; // 장소 이미지1
    private String imageUrl2; // 장소 이미지2
    private String imageUrl3; // 장소 이미지3

    private String time; // 영업시간(영업 시작 ~ 종료시간)

    @Column(length = 300)
    private String homepageUrl; // 홈페이지 주소

    @Column(length = 500)
    private String description; // 설명

    private Float rating; // 별점

    private Integer reviewCount; // 리뷰 개수

    private Integer price; // 평균 가격

    @Column(length = 500)
    private String veganType; // 비건 유형

    @Column(length = 500)
    private String foodType; // 음식 종류 유형

    @Column(length = 500)
    private String visitType; // 관람 유형

    @Column(length = 500)
    private String indoorOutdoorType; // 실내외 유형

    @Column(length = 500)
    private String cafeType; // 카페 유형

    @Column(length = 500)
    private String admissionFee; // 입장료

    @Column(length = 500)
    private String checkInTime; // 체크인 시간

    @Column(length = 500)
    private String checkOutTime; // 체크아웃 시간

    @Column(length = 500)
    private String dailyStayFee; // 하루 숙박료

    @Column(length = 500)
    private String operationStartTime; // 영업 시작 시간

    @Column(length = 500)
    private String operationEndTime; // 영업 종료 시간

    @Column(length = 500)
    private String restDay; // 휴무일

    @Column(length = 500)
    private String viewTime; // 관람 시간

    @Column(length = 500)
    private String note; // 비고

    @Column(length = 500)
    private String usage; // 이용 방법

    private Boolean hasSwimmingPool; // 수영장 유무

    @Column(length = 500)
    private String priceRange; // 가격대

    @Column(length = 1000)
    private String relatedLink; // 관련 링크

    @Column(length = 500)
    private Integer totalRating; // 총 별점

    @OneToMany(mappedBy = "place")
    private List<Like> likes;

    @OneToMany(mappedBy = "place")
    private List<VisitedPlace> visitedPlaces;
}