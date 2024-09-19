package com.snowflake_guide.tourmate.domain.baggage_storage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaggageStorageResponseDto {
    private Long baggageStorageId;
    private int lineNumber;
    private String lockerName;
    private double latitude;
    private double longitude;
    private String lockerDetail; // 장소명
}