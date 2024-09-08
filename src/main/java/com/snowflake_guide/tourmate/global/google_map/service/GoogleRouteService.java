package com.snowflake_guide.tourmate.global.google_map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowflake_guide.tourmate.global.client.GoogleDirectionsClient;
import com.snowflake_guide.tourmate.global.google_map.dto.GoogleRouteRequestDto;
import com.snowflake_guide.tourmate.global.google_map.dto.GoogleRouteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleRouteService {

    private final GoogleDirectionsClient googleDirectionsClient;
    private final ObjectMapper objectMapper;  // JSON 파싱용 ObjectMapper

    @Value("${google.api.key}")
    private String apiKey;

    public GoogleRouteResponseDto getTransitRouteSummary(GoogleRouteRequestDto requestDto) {
        String waypoints = "optimize:true|" + String.join("|", requestDto.getWaypoints());
        String response = googleDirectionsClient.getDirections(
                requestDto.getOrigin(),
                requestDto.getDestination(),
                waypoints,
                "transit",  // 대중교통 모드
                apiKey
        );

        GoogleRouteResponseDto responseDto = new GoogleRouteResponseDto();
        List<GoogleRouteResponseDto.TransitDetailsDto> transitDetailsList = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode legs = root.path("routes").get(0).path("legs");

            // 각 구간(A->B, B->C 등)별로 대중교통 정보 추출
            for (JsonNode leg : legs) {
                String startAddress = leg.path("start_address").asText();
                String endAddress = leg.path("end_address").asText();

                for (JsonNode step : leg.path("steps")) {
                    if (step.has("transit_details")) {
                        JsonNode transitDetails = step.path("transit_details");
                        String vehicleType = transitDetails.path("line").path("vehicle").path("type").asText();  // 대중교통 수단 유형
                        String lineName = transitDetails.path("line").has("short_name")
                                ? transitDetails.path("line").path("short_name").asText()
                                : transitDetails.path("line").path("name").asText();  // 버스/지하철 노선 또는 이름
                        String departureStop = transitDetails.path("departure_stop").has("name")
                                ? transitDetails.path("departure_stop").path("name").asText()
                                : "N/A";  // 출발 정류장 (없을 경우 "N/A")
                        String arrivalStop = transitDetails.path("arrival_stop").has("name")
                                ? transitDetails.path("arrival_stop").path("name").asText()
                                : "N/A";  // 도착 정류장 (없을 경우 "N/A")
                        String duration = step.path("duration").path("text").asText();  // 걸리는 시간
                        String distance = step.path("distance").path("text").asText();  // 거리

                        // 각 구간의 대중교통 정보 저장
                        GoogleRouteResponseDto.TransitDetailsDto detailsDto = new GoogleRouteResponseDto.TransitDetailsDto();
                        detailsDto.setVehicleType(vehicleType);
                        detailsDto.setLineName(lineName);
                        detailsDto.setDepartureStop(departureStop);
                        detailsDto.setArrivalStop(arrivalStop);
                        detailsDto.setDuration(duration);
                        detailsDto.setDistance(distance);
                        detailsDto.setStartAddress(startAddress);
                        detailsDto.setEndAddress(endAddress);

                        transitDetailsList.add(detailsDto);
                    }
                }
            }

            // 전체 경로의 overview_polyline.points를 추출하여 저장
            String polyline = root.path("routes")
                    .get(0)
                    .path("overview_polyline")
                    .path("points")
                    .asText();
            responseDto.setPolyline(polyline);  // 전체 경로 폴리라인 설정
            responseDto.setTransitDetailsList(transitDetailsList);
        } catch (IOException e) {
            log.error("Failed to parse Google Directions API response", e);
            throw new RuntimeException("대중교통 경로 계산 중 오류가 발생했습니다.");
        }

        return responseDto;
    }
}