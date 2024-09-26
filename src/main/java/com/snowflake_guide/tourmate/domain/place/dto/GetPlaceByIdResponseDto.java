package com.snowflake_guide.tourmate.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드를 제외하고 직렬화
public class GetPlaceByIdResponseDto {

    private Long placeId;

    private Long themeId;

    @JsonProperty("테마이름")
    private String placeTheme;

    @JsonProperty("위도")
    private Float latitude;

    @JsonProperty("경도")
    private Float longitude;

    @JsonProperty("장소명")
    private String placeName;

    @JsonProperty("주소명")
    private String placeLocation;

    @JsonProperty("이미지")
    private String imageUrl1;

    @JsonProperty("이미지2")
    private String imageUrl2;

    @JsonProperty("이미지3")
    private String imageUrl3;

    @JsonProperty("영업시간")
    private String time;

    @JsonProperty("홈페이지 URL")
    private String homepageUrl;

    @JsonProperty("설명")
    private String description;

    @JsonProperty("평점")
    private Float rating;

    @JsonProperty("리뷰수")
    private Integer reviewCount;

    @JsonProperty("가격")
    private Integer price;

    @JsonProperty("비건 유형")
    private String veganType;

    @JsonProperty("음식 종류")
    private String foodType;

    @JsonProperty("방문 유형")
    private String visitType;

    @JsonProperty("실내외 유형")
    private String indoorOutdoorType;

    @JsonProperty("카페 유형")
    private String cafeType;

    @JsonProperty("입장료")
    private String admissionFee;

    @JsonProperty("체크인 시간")
    private String checkInTime;

    @JsonProperty("체크 아웃 시간")
    private String checkOutTime;

    @JsonProperty("숙박료")
    private String dailyStayFee;

    @JsonProperty("영업 시작 시간")
    private String operationStartTime;

    @JsonProperty("영업 종료 시간")
    private String operationEndTime;

    @JsonProperty("휴무일")
    private String restDay;

    @JsonProperty("관람 시간")
    private String viewTime;

    @JsonProperty("비고")
    private String note;

    @JsonProperty("이용 방법")
    private String usage;

    @JsonProperty("수영장 유무")
    private Boolean hasSwimmingPool;

    @JsonProperty("가격대")
    private String priceRange;

    @JsonProperty("관련 링크")
    private String relatedLink;

    @JsonProperty("총 별점")
    private Integer totalRating;

    private boolean visited; // 사용자의 방문 여부
    private boolean likes;
    // Place 엔티티를 받아 필요한 필드만 초기화
    public static GetPlaceByIdResponseDto fromEntity(Place place, Boolean visited, Boolean likes) {
        return new GetPlaceByIdResponseDto(
                place.getPlaceId(),
                place.getTheme() != null ? place.getTheme().getThemeId() : null,
                place.getTheme() != null ? place.getTheme().getPlaceTheme() : null,
                place.getLatitude(),
                place.getLongitude(),
                place.getPlaceName(),
                place.getPlaceLocation(),
                place.getImageUrl1(),
                place.getImageUrl2(),
                place.getImageUrl3(),
                place.getTime(),
                place.getHomepageUrl(),
                place.getDescription(),
                place.getRating(),
                place.getReviewCount(),
                place.getPrice(),
                place.getVeganType(),
                place.getFoodType(),
                place.getVisitType(),
                place.getIndoorOutdoorType(),
                place.getCafeType(),
                place.getAdmissionFee(),
                place.getCheckInTime(),
                place.getCheckOutTime(),
                place.getDailyStayFee(),
                place.getOperationStartTime(),
                place.getOperationEndTime(),
                place.getRestDay(),
                place.getViewTime(),
                place.getNote(),
                place.getUsage(),
                place.getHasSwimmingPool(),
                place.getPriceRange(),
                place.getRelatedLink(),
                place.getTotalRating(),
                visited,
                likes
        );
    }
}
