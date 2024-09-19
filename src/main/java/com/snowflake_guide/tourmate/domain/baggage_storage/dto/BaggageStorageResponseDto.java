package com.snowflake_guide.tourmate.domain.baggage_storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BaggageStorageResponseDto {
    private String parentName;
    private Long baggageStorageId;
    private int lineNumber;
    private double longitude;
    private double latitude;
}