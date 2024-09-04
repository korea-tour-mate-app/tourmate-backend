package com.snowflake_guide.tourmate.global.tmap.routeopt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * t-map routeOptimization API에 요청할 때 사용하는 dto
 */
@Getter
public class TMapRouteOptRequestDto {
    private String startName;  // 출발지 명칭
    private String startX;  // 출발지 X좌표 (경도)
    private String startY;  // 출발지 Y좌표 (위도)
    private String startTime;  // 출발 시간 (YYYYMMDDHHMM) // 2시간
    private String endName;  // 목적지 명칭
    private String endX;  // 목적지 X좌표 (경도)
    private String endY;  // 목적지 Y좌표 (위도)
    private List<ViaPoint> viaPoints;  // 경유지 목록

    @Getter
    @Setter
    public static class ViaPoint {
        private String viaPointId;  // 경유지 ID (PK)
        private String viaPointName;  // 경유지 명칭
        private String viaX;  // 경유지 X좌표 (경도)
        private String viaY;  // 경유지 Y좌표 (위도)
    }
}
