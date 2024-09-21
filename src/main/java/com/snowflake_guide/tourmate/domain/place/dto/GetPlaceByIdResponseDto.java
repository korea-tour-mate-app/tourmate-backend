package com.snowflake_guide.tourmate.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String placeTheme;
    private Float latitude;
    private Float longitude;
    private String placeName;
    private String placeLocation;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String startEndTime;
    private String deadlineTime;
    private String placeCost;
    private String checkInOutTime;
    private String phoneNumber;
    private String dayOff;
    private String duringTime;
    private Integer templateStayType;
    private String homepageUrl;
    private Float avgCost;
    private Integer veganType;
    private Integer foodType;
    private Float rate;
    private Integer viewType;
    private Integer inoutType;
    private Integer cafeType;

    // Place 엔티티를 받아 필요한 필드만 초기화
    public static GetPlaceByIdResponseDto fromEntity(Place place) {
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
                place.getStartEndTime(),
                place.getDeadlineTime(),
                place.getPlaceCost(),
                place.getCheckInOutTime(),
                place.getPhoneNumber(),
                place.getDayOff(),
                place.getDuringTime(),
                place.getTemplateStayType() == 0 ? null : place.getTemplateStayType(),
                place.getHomepageUrl(),
                place.getAvgCost() == null || place.getAvgCost() == 0.0f ? null : place.getAvgCost(),
                place.getVeganType() == 0 ? null : place.getVeganType(),
                place.getFoodType() == 0 ? null : place.getFoodType(),
                place.getRate() == null || place.getRate() == 0.0f ? null : place.getRate(),
                place.getViewType() == 0 ? null : place.getViewType(),
                place.getInoutType() == 0 ? null : place.getInoutType(),
                place.getCafeType() == 0 ? null : place.getCafeType()
        );
    }
}