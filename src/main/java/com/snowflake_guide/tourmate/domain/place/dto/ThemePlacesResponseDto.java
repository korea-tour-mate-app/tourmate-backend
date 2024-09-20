package com.snowflake_guide.tourmate.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThemePlacesResponseDto {

    private Long themeId;
    private String placeTheme;
    private List<PlaceDto> places;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceDto {
        private Float latitude;
        private Float longitude;
        private String placeName;
        private Long placeId;
    }
}