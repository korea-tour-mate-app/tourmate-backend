package com.snowflake_guide.tourmate.global.google_api.dto;

import lombok.Getter;

@Getter
public class GooglePlacesAPIRequestDto {
    private String location;  // 검색할 중심 위치 (예: "37.5665,126.9780")
    private int radius;       // 검색 반경 (예: 1000 미터)
}
