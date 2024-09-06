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

import java.util.ArrayList;
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

    private static final String TMAP_API_URL = "https://apis.openapi.sk.com/tmap/routes/routeOptimization10?version=1";

    public RouteOptResponseDto optimizeRoute(TMapRouteOptRequestDto requestDto) {
        System.out.println("여기 들어옴");
        try {
            // Request Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json"); // json 포맷의 데이터 응답 (Defalut)
            headers.set("appKey", apiKey); // appKey를 헤더에 설정

//            // Querystring에 version을 추가하여 URL을 빌드 -> 하드코딩으로 대체
//            String urlWithParams = UriComponentsBuilder.fromHttpUrl(TMAP_API_URL)
//                    .queryParam("version", apiVersion)  // 필수 파라미터 version 추가
//                    .toUriString();

            // HttpEntity로 요청 본문과 헤더 설정
            HttpEntity<TMapRouteOptRequestDto> entity = new HttpEntity<>(requestDto, headers);
            log.info("TMap API 요청 준비 완료");

            // TMap API에 POST 요청
            ResponseEntity<TMapRouteOptResponseDto> response = restTemplate.exchange(TMAP_API_URL, HttpMethod.POST, entity, TMapRouteOptResponseDto.class);

            // 응답 데이터가 null인 경우 처리
            // 응답 데이터를 가공하여 RouteOptResponseDto로 변환
            TMapRouteOptResponseDto tMapResponse = response.getBody();
            if (tMapResponse == null) {
                throw new RuntimeException("TMap API 응답이 비어있습니다.");
            }
            log.info("TMap API 응답: {}", tMapResponse);

            // RouteOptResponseDto로 필요한 정보만 추출
            // 경로의 총 거리, 총 시간, 총 요금 설정
            RouteOptResponseDto responseDto = new RouteOptResponseDto();
            responseDto.setTotalDistance(tMapResponse.getProperties().getTotalDistance());
            responseDto.setTotalTime(tMapResponse.getProperties().getTotalTime());
            responseDto.setTotalFare(tMapResponse.getProperties().getTotalFare());

            // 경로 정보 추출 및 변환 (LineString 타입만 필터링)
            List<RouteOptResponseDto.Path> paths = tMapResponse.getFeatures().stream()
                    .filter(feature -> feature.getGeometry().getType().equals("LineString"))  // LineString 타입만 필터링
                    .map(feature -> {
                        RouteOptResponseDto.Path path = new RouteOptResponseDto.Path();

                        // LineString 좌표 처리
                        List<List<Double>> coordinates = feature.getGeometry().getLineStringCoordinates().stream()
                                .map(coord -> {
                                    List<Double> latLng = new ArrayList<>();
                                    latLng.add(coord.get(1));  // Y좌표 -> latitude
                                    latLng.add(coord.get(0));  // X좌표 -> longitude
                                    return latLng;
                                })
                                .toList();
                        path.setCoordinates(coordinates);
                        path.setName(feature.getProperties().getViaPointName()); // 장소명 설정
                        return path;
                    })
                    .collect(Collectors.toList());

            log.info("경로 정보 변환 완료: {}개의 경로", paths.size());
            responseDto.setPaths(paths);

            return responseDto;

        } catch (Exception e) {
            log.error("TMap API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("경유지 최적화 요청 실패", e); // 원본 예외도 포함하여 던짐
        }
    }
}