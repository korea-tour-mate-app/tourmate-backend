package com.snowflake_guide.tourmate.domain.visited_place.dto;

import com.snowflake_guide.tourmate.domain.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisitedPlaceListResponseDto {
    private Long placeId;
    private String placeName;
    private String placeLocation;
    private Float latitude;
    private Float longitude;

    public VisitedPlaceListResponseDto(Place place) {
        this.placeId = place.getPlaceId();
        this.placeName = place.getPlaceName();
        this.placeLocation = place.getPlaceLocation();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
    }
}
