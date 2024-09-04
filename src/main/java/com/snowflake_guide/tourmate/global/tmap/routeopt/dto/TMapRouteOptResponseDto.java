package com.snowflake_guide.tourmate.global.tmap.routeopt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * TMap routeOptimization API로부터 응답받을 때 사용하는 dto
 */
@Getter
@Setter
public class TMapRouteOptResponseDto {

    private String type;  // GeoJSON 표준 프로퍼티
    private Properties properties;  // 전체 경로 정보 (거리, 시간, 요금)
    private List<Feature> features;  // 경유지 정보 리스트

    @Getter
    @Setter
    public static class Properties {
        private String totalDistance;  // 전체 경로 거리
        private String totalTime;  // 전체 소요 시간
        private String totalFare;  // 전체 요금
    }

    @Getter
    @Setter
    public static class Feature {
        private String type;  // GeoJSON 표준 프로퍼티 (Feature)
        private Geometry geometry;  // 경유지의 좌표 정보
        private FeatureProperties properties;  // 경유지 관련 정보

        @Getter
        @Setter
        public static class Geometry {
            private String type;  // "Point" 타입 (경유지 좌표)
            private List<Double> coordinates;  // 경도, 위도 좌표
        }

        @Getter
        @Setter
        public static class FeatureProperties {
            private String index;  // 경유지 순서 (0부터 시작)
            private String viaPointId;  // 경유지 ID
            private String viaPointName;  // 경유지 명칭
            private String arriveTime;  // 도착 시간
            private String completeTime;  // 완료 시간
            private String distance;  // 해당 경유지까지의 거리
            private String deliveryTime;  // 배달 시간
            private String waitTime;  // 대기 시간
            private String pointType;  // 경유지의 타입 ("S"는 출발지, "E"는 목적지 등)
        }
    }
}
