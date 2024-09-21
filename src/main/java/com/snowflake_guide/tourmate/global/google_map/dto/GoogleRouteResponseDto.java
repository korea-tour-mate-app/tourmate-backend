//package com.snowflake_guide.tourmate.global.google_map.dto;
//
//import lombok.Builder;
//import lombok.Data;
//import java.util.List;
//
//@Data
//@Builder
//public class GoogleRouteResponseDto {
//
//    // 대중교통 구간 정보 리스트
//    private List<TransitDetailsDto> transitDetailsList;
//
//    // 전체 경로 폴리라인 정보
//    private String polyline;
//
//    @Data
//    @Builder
//    public static class TransitDetailsDto {
//
//        // 대중교통 수단 유형 (예: 버스, 지하철, 기차 등)
//        private String vehicleType;
//
//        // 버스/지하철 노선 또는 이름
//        private String lineName;
//
//        // 출발/도착 정류장 (정류장이 없으면 "N/A")
//        private String departureStop;
//        private String arrivalStop;
//
//        // 각 구간의 소요 시간과 거리
//        private String duration;
//        private String distance;
//
//        // 구간의 시작/끝 지점 주소
//        private String startAddress;
//        private String endAddress;
//    }
//}
