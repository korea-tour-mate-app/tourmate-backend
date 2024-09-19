package com.snowflake_guide.tourmate.domain.baggage_storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaggageStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long baggageStorageId;

    private int lineNumber;
    private String lockerName;
    private String lockerDetail;
    private double latitude; // 위도
    private double longitude; // 경도
    private int smallCount;
    private int mediumCount;
    private int largeCount;
    private int controllerCount;
    private int columnCount;
}
