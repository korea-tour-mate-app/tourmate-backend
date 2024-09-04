package com.snowflake_guide.tourmate.global.tmap.routeopt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * /api/tmap/optimize-route API에 응답할 때 사용하는 dto
 */
@Setter
@Getter
public class RouteOptResponseDto {
    // 경로 관련 정보
    private String totalDistance;  // 경로 총 거리 (단위: m)
    private String totalTime;      // 경로 총 소요 시간 (단위: 초)
    private String totalFare;      // 경로 총 요금 (단위: 원)

    // 경로(길) 정보 리스트
    private List<Path> paths;

    // 경로(길)를 표현하는 클래스
    @Getter
    @Setter
    public static class Path {
        private List<List<Double>> coordinates;  // 경로 좌표 리스트 (경도, 위도의 배열)
        private String name;  // 장소명 (viaPointName)
    }
}
