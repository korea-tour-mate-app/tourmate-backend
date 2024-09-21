package com.snowflake_guide.tourmate.global.tmap.routeopt.service;

import com.snowflake_guide.tourmate.global.client.TMapRouteOptClient;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.RouteOptResponseDto;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.TMapRouteOptRequestDto;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.TMapRouteOptResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteOptService {

    private final RestTemplate restTemplate;
    private final TMapRouteOptClient tMapRouteOptClient;

    // application.properties에서 API 키를 주입받음
    @Value("${tmap.api.key}")
    private String apiKey;
    private static final String API_VERSION = "1";

    public RouteOptResponseDto optimizeRoute(TMapRouteOptRequestDto requestDto) {
        try {
            // 경유지가 null이 아니고 비어 있지 않은 경우에만 가장 멀리 떨어진 지점을 계산
            if (requestDto.getViaPoints() != null && !requestDto.getViaPoints().isEmpty()) {
                // 가장 멀리 떨어진 두 지점을 찾아 출발지와 도착지로 설정
                requestDto = findFarthestPoints(requestDto);
            }
            log.info("TMapRouteOptRequestDto는 {}:", requestDto.toString());
            log.info("TMap API 요청 준비 완료");

            // TMap API 호출
            TMapRouteOptResponseDto tMapResponse = tMapRouteOptClient.optimizeRoute(API_VERSION, apiKey, requestDto);

            // 응답 데이터가 null인 경우 처리
            if (tMapResponse == null) {
                throw new RuntimeException("TMap API 응답이 비어있습니다.");
            }
            log.info("TMap API 응답: {}", tMapResponse.toString());

            // RouteOptResponseDto로 필요한 정보만 추출
            RouteOptResponseDto responseDto = new RouteOptResponseDto();
            getSum(responseDto, tMapResponse);
            getSeq(tMapResponse, responseDto);
            List<RouteOptResponseDto.Path> paths = getPaths(tMapResponse);

            log.info("경로 정보 변환 완료: {}개의 경로", paths.size());
            responseDto.setPaths(paths);

            return responseDto;

        } catch (Exception e) {
            log.error("TMap API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("경유지 최적화 요청 실패", e); // 원본 예외도 포함하여 던짐
        }
    }

    // 전체 방문장소 순서를 포함한 장소 리스트 반환
    private static void getSeq(TMapRouteOptResponseDto tMapResponse, RouteOptResponseDto responseDto) {
        List<RouteOptResponseDto.VisitPlace> visitPlaces = new ArrayList<>();

        // "type"이 "Point"인 feature만 처리
        tMapResponse.getFeatures().stream()
                .filter(feature -> "Point".equals(feature.getGeometry().getType()))  // Point 타입만 필터링
                .forEach(feature -> {
                    RouteOptResponseDto.VisitPlace visitPlace = new RouteOptResponseDto.VisitPlace();

                    // viaPointName을 가져와 설정
                    String viaPointName = feature.getProperties().getViaPointName();
                    if (viaPointName != null) {
                        // [숫자]로 시작하는 부분을 제거
                        viaPointName = viaPointName.replaceAll("^\\[\\d+\\]\\s*", "");
                    }
                    visitPlace.setName(viaPointName);

                    // 경로순번인 index를 가져와 설정
                    String index = feature.getProperties().getIndex();
                    visitPlace.setOrder(index);  // AtomicInteger로 순서 증가


                    // coordinates에서 경도, 위도 설정
                    List<?> coordinates = feature.getGeometry().getCoordinates();
                    if (coordinates != null && coordinates.size() >= 2) {
                        // 명시적으로 Object를 Double로 변환
                        visitPlace.setLongitude(((Number) coordinates.get(0)).doubleValue());  // 경도 (X좌표)
                        visitPlace.setLatitude(((Number) coordinates.get(1)).doubleValue());   // 위도 (Y좌표)
                    }
                    // 방문 장소 리스트에 추가
                    visitPlaces.add(visitPlace);
                });

        // 최종적으로 responseDto에 visitPlaces 리스트 설정
        responseDto.setVisitPlaces(visitPlaces);
    }


    // 경로의 총 거리, 총 시간, 총 요금 설정
    private static void getSum(RouteOptResponseDto responseDto, TMapRouteOptResponseDto tMapResponse) {
        responseDto.setTotalDistance(tMapResponse.getProperties().getTotalDistance());
        responseDto.setTotalTime(tMapResponse.getProperties().getTotalTime());
        responseDto.setTotalFare(tMapResponse.getProperties().getTotalFare());
    }

    // 경로 정보 추출 및 변환 (LineString 타입만 필터링)
    private static List<RouteOptResponseDto.Path> getPaths(TMapRouteOptResponseDto tMapResponse) {
        return tMapResponse.getFeatures().stream()
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
    }


    // 두 좌표 간의 거리를 계산하는 하버사인 공식
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // 지구 반지름 (킬로미터)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 반환 (킬로미터)
    }

    // 가장 멀리 떨어진 두 장소를 찾는 함수
    private static TMapRouteOptRequestDto findFarthestPoints(TMapRouteOptRequestDto requestDto) {
        List<TMapRouteOptRequestDto.ViaPoint> allPoints = getStartAndEndPoint(requestDto);

        // 경유지가 존재하는 경우 모든 지점을 담는 allPoints배열에 추가
        if (requestDto.getViaPoints() != null && !requestDto.getViaPoints().isEmpty()) {
            allPoints.addAll(requestDto.getViaPoints());
        }
        // 경유지가 없는 경우 인자값 그대로 반환
        else {
            return requestDto;
        }

        double maxDistance = 0;
        TMapRouteOptRequestDto.ViaPoint farthestStart = null;
        TMapRouteOptRequestDto.ViaPoint farthestEnd = null;

        // 모든 지점 간의 거리를 계산하여 가장 멀리 떨어진 두 지점을 찾음
        for (TMapRouteOptRequestDto.ViaPoint point1 : allPoints) {
            for (TMapRouteOptRequestDto.ViaPoint point2 : allPoints) {
                if (!point1.equals(point2)) {
                    double distance = calculateDistance(
                            Double.parseDouble(point1.getViaY()),
                            Double.parseDouble(point1.getViaX()),
                            Double.parseDouble(point2.getViaY()),
                            Double.parseDouble(point2.getViaX())
                    );
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        farthestStart = point1;
                        farthestEnd = point2;
                    }
                }
            }
        }

        // 가장 멀리 떨어진 두 지점을 출발지와 도착지로 설정
        if (farthestStart != null && farthestEnd != null) {
            requestDto.setStartName(farthestStart.getViaPointName());
            requestDto.setStartX(farthestStart.getViaX());
            requestDto.setStartY(farthestStart.getViaY());

            requestDto.setEndName(farthestEnd.getViaPointName());
            requestDto.setEndX(farthestEnd.getViaX());
            requestDto.setEndY(farthestEnd.getViaY());

            // 경유지에서 가장 멀리 떨어진 두 지점은 제외하고 설정
            List<TMapRouteOptRequestDto.ViaPoint> updatedViaPoints = new ArrayList<>(allPoints);
            updatedViaPoints.remove(farthestStart);
            updatedViaPoints.remove(farthestEnd);
            requestDto.setViaPoints(updatedViaPoints);
        } else {
            log.warn("가장 멀리 떨어진 지점을 찾지 못했습니다. 기본값을 유지합니다.");
        }
        return requestDto;
    }

    // 출발지와 목적지에 ViaPointId 필드를 각각 추가
    private static List<TMapRouteOptRequestDto.ViaPoint> getStartAndEndPoint(TMapRouteOptRequestDto requestDto) {
        List<TMapRouteOptRequestDto.ViaPoint> allPoints = new ArrayList<>();

        // 출발지, 도착지를 ViaPoint로 변환하여 리스트에 추가
        TMapRouteOptRequestDto.ViaPoint startPoint = new TMapRouteOptRequestDto.ViaPoint();
        startPoint.setViaPointId("start");
        startPoint.setViaPointName(requestDto.getStartName());
        startPoint.setViaX(requestDto.getStartX());
        startPoint.setViaY(requestDto.getStartY());
        allPoints.add(startPoint);

        TMapRouteOptRequestDto.ViaPoint endPoint = new TMapRouteOptRequestDto.ViaPoint();
        endPoint.setViaPointId("end");
        endPoint.setViaPointName(requestDto.getEndName());
        endPoint.setViaX(requestDto.getEndX());
        endPoint.setViaY(requestDto.getEndY());
        allPoints.add(endPoint);
        return allPoints;
    }
}