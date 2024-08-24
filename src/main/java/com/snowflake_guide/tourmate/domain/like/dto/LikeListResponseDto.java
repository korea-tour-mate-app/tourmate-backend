package com.snowflake_guide.tourmate.domain.like.dto;

import com.snowflake_guide.tourmate.domain.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeListResponseDto {
    private Long placeId;
    private String placeName;
    private String placeLocation;
    private Float latitude;
    private Float longitude;

    public LikeListResponseDto(Place place) {
        this.placeId = place.getPlaceId();
        this.placeName = place.getPlaceName();
        this.placeLocation = place.getPlaceLocation();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
    }
}
