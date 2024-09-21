package com.snowflake_guide.tourmate.domain.baggage_storage.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaggageStorageDetailResponseDto {
    private String lockerName;
    private String lockerDetail; // 세부 위치 정보
    private int smallCount;
    private int mediumCount;
    private int largeCount;
    private int controllerCount;
    private int columnCount;
}
