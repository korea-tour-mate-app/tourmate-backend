package com.snowflake_guide.tourmate.global.tmap.routeopt.service;

import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.RouteOptResponseDto;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.TMapRouteOptRequestDto;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.TMapRouteOptResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteOptService {

    private final RestTemplate restTemplate;

    // application.properties에서 API 키를 주입받음
    @Value("${tmap.api.key}")
    private String apiKey;

    @Value("${tmap.api.version}")
    private String apiVersion;

    private static final String TMAP_API_URL = "https://apis.openapi.sk.com/tmap/routes/routeOptimization10?version=1";

    public RouteOptResponseDto optimizeRoute(TMapRouteOptRequestDto requestDto) {

        try {
            // Request Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json"); // json 포맷의 데이터 응답 (Defalut)
            headers.set("appKey", apiKey); // appKey를 헤더에 설정

            // Querystring에 version을 추가하여 URL을 빌드
            String urlWithParams = UriComponentsBuilder.fromHttpUrl(TMAP_API_URL)
                    .queryParam("version", apiVersion)  // 필수 파라미터 version 추가
                    .toUriString();

            // HttpEntity로 요청 본문과 헤더 설정
            HttpEntity<TMapRouteOptRequestDto> entity = new HttpEntity<>(requestDto, headers);

            // TMap API에 POST 요청
            ResponseEntity<TMapRouteOptResponseDto> response = restTemplate.exchange(TMAP_API_URL, HttpMethod.POST, entity, TMapRouteOptResponseDto.class);

            // 응답 데이터를 가공하여 RouteOptResponseDto로 변환
            TMapRouteOptResponseDto tMapResponse = response.getBody();

            // RouteOptResponseDto로 필요한 정보만 추출
            RouteOptResponseDto responseDto = new RouteOptResponseDto();

            // 경로의 총 거리, 총 시간, 총 요금 설정
            assert tMapResponse != null;
            responseDto.setTotalDistance(tMapResponse.getProperties().getTotalDistance());
            responseDto.setTotalTime(tMapResponse.getProperties().getTotalTime());
            responseDto.setTotalFare(tMapResponse.getProperties().getTotalFare());

            // 경로(길) 정보 추출 및 변환 (LineString 타입만 필터링)
            List<RouteOptResponseDto.Path> paths = tMapResponse.getFeatures().stream()
                    .filter(feature -> feature.getGeometry().getType().equals("LineString"))  // LineString 타입만 필터링
                    .map(feature -> {
                        RouteOptResponseDto.Path path = new RouteOptResponseDto.Path();
                        path.setCoordinates(Collections.singletonList(feature.getGeometry().getCoordinates())); // 경로(좌표 배열) 설정
                        path.setName(feature.getProperties().getViaPointName()); // 장소명 설정
                        return path;
                    })
                    .collect(Collectors.toList());

            responseDto.setPaths(paths);
            return responseDto;
        } catch (Exception e) {
            log.error("TMap API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("경유지 최적화 요청 실패");
        }
    }
}