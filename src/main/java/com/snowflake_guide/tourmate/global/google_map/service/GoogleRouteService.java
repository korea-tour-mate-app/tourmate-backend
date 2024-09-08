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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleRouteService {

    private final GoogleDirectionsClient googleDirectionsClient;
    private final ObjectMapper objectMapper;

    @Value("${google.api.key}")
    private String apiKey;

    public GoogleRouteResponseDto getTransitRouteSummary(GoogleRouteRequestDto requestDto) {
        String waypoints = "optimize:true|" + String.join("|", requestDto.getWaypoints());
        String response = googleDirectionsClient.getDirections(
                requestDto.getOrigin(),
                requestDto.getDestination(),
                waypoints,
                "transit", // 대중교통 모드
                apiKey
        );

        try {
            return parseDirectionsResponse(response);
        } catch (IOException e) {
            log.error("Failed to parse Google Directions API response: {}", e.getMessage());
            throw new RuntimeException("대중교통 경로 계산 중 오류가 발생했습니다.", e);
        }
    }

    private GoogleRouteResponseDto parseDirectionsResponse(String response) throws IOException {
        JsonNode root = objectMapper.readTree(response);
        List<GoogleRouteResponseDto.TransitDetailsDto> transitDetailsList = new ArrayList<>();

        JsonNode legs = root.path("routes").get(0).path("legs");
        for (JsonNode leg : legs) {
            String startAddress = leg.path("start_address").asText();
            String endAddress = leg.path("end_address").asText();

            for (JsonNode step : leg.path("steps")) {
                if (step.has("transit_details")) {
                    JsonNode transitDetails = step.path("transit_details");

                    transitDetailsList.add(GoogleRouteResponseDto.TransitDetailsDto.builder()
                            .vehicleType(transitDetails.path("line").path("vehicle").path("type").asText())
                            .lineName(transitDetails.path("line").has("short_name")
                                    ? transitDetails.path("line").path("short_name").asText()
                                    : transitDetails.path("line").path("name").asText())
                            .departureStop(transitDetails.path("departure_stop").has("name")
                                    ? transitDetails.path("departure_stop").path("name").asText()
                                    : "N/A")
                            .arrivalStop(transitDetails.path("arrival_stop").has("name")
                                    ? transitDetails.path("arrival_stop").path("name").asText()
                                    : "N/A")
                            .duration(step.path("duration").path("text").asText())
                            .distance(step.path("distance").path("text").asText())
                            .startAddress(startAddress)
                            .endAddress(endAddress)
                            .build());
                }
            }
        }

        String polyline = root.path("routes").get(0).path("overview_polyline").path("points").asText();

        return GoogleRouteResponseDto.builder()
                .polyline(polyline)
                .transitDetailsList(transitDetailsList)
                .build();
    }
}