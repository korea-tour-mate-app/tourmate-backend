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
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @Column(nullable = false)
    private String placeName; // 여행지

    @Column(nullable = false, length = 300)
    private String placeLocation; // 주소

    @Column(nullable = false, length = 300)
    private String imageUrl1; // 장소 이미지1
    private String imageUrl2; // 장소 이미지2
    private String imageUrl3; // 장소 이미지3
    private String startEndTime; // 영업시간(영업 시작 ~ 종료시간)
    private String deadlineTime; // 입장마감 시간
    private String placeCost; // 입장료
    private String checkInOutTime; // 체크인, 체크아웃시간
    private String phoneNumber; // 전화번호
    private String dayOff; // 휴무일
    private String duringTime; // 관람시간
//    private String stayInCost; // 하루숙박료
    private int templateStayType; // 템플스테이유형

    @Column(nullable = false, length = 300)
    private String homepageUrl; // 홈페이지 주소
    private Float avgCost; // 평균 가격
    private int veganType; // 비건유형
    private int foodType; // 음식유형
    private Float rate; // 별점
    private int viewType; // 관람유형
    private int inoutType; // 실내외유형
    private int cafeType; // 카페유형

    @OneToMany(mappedBy = "place")
    private List<Like> likes;

    @OneToMany(mappedBy = "place")
    private List<VisitedPlace> visitedPlaces;
}

