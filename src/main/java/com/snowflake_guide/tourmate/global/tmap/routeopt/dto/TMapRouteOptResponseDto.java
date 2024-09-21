package com.snowflake_guide.tourmate.global.tmap.routeopt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
            private String type;  // "Point" 또는 "LineString" 타입

            // Point일 경우 List<Double>, LineString일 경우 List<List<Double>>로 처리
            private List<?> coordinates;

            // 좌표가 Point일 경우
            public List<Double> getPointCoordinates() {
                if ("Point".equals(type)) {
                    return (List<Double>) coordinates;
                }
                throw new IllegalStateException("Geometry 타입이 Point가 아닙니다.");
            }

            // 좌표가 LineString일 경우
            public List<List<Double>> getLineStringCoordinates() {
                if ("LineString".equals(type)) {
                    return (List<List<Double>>) coordinates;
                }
                throw new IllegalStateException("Geometry 타입이 LineString이 아닙니다.");
            }
        }

        @Getter
        @Setter
        public static class FeatureProperties {
            private String index;  // 경유지 순서 (0부터 시작)
            private String viaPointId;  // 경유지 ID (nullable)
            private String viaPointName;  // 경유지 명칭
            private String viaDetailAddress;  // 경유지 상세주소 (nullable)
            private String groupKey;  // 그룹키 (nullable)
            private String arriveTime;  // 도착 시간
            private String completeTime;  // 완료 시간
            private String distance;  // 해당 경유지까지의 거리
            private String deliveryTime;  // 배달 시간
            private String waitTime;  // 대기 시간
            private String time;  // 경유지 간 소요 시간 (nullable)
            private String Fare;  // 경유지 간 요금 (nullable), JSON 응답의 "Fare" 대소문자 맞춤
            private String pointType;  // 경유지의 타입 ("S"는 출발지, "E"는 목적지, "B"는 경유지 등)
            private String poiId;  // POI ID (nullable)
        }
    }
}
