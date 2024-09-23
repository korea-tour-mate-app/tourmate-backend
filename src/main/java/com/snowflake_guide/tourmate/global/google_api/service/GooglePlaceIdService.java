package com.snowflake_guide.tourmate.global.google_api.service;

import com.snowflake_guide.tourmate.domain.restaurant.service.FindRestaurantService;
import com.snowflake_guide.tourmate.global.client.GooglePlaceIdClient;
import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlacesAPIResponseDto;
import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlaceIdService {
    private final GooglePlaceIdClient googlePlaceIdClient;
    private final FindRestaurantService findRestaurantService;

    @Value("${google.api.key}")
    private String apiKey;


    // 구별 필요한 데이터 수를 설정
    private static final Map<String, Integer> REQUIRED_DATA = new HashMap<>() {{
        put("중구", 52);
        put("강남구", 52);
        put("용산구", 52);
        put("종로구", 52);
        put("서초구", 42);
        put("영등포구", 42);
        put("마포구", 42);
        put("성동구", 42);
        put("성북구", 42);
        put("광진구", 42);
        put("송파구", 42);
        // 나머지 구는 12개씩
        put("강동구", 12);
        put("강북구", 12);
        put("강서구", 12);
        put("관악구", 12);
        put("구로구", 12);
        put("금천구", 12);
        put("노원구", 12);
        put("도봉구", 12);
        put("동대문구", 12);
        put("동작구", 12);
        put("서대문구", 12);
        put("양천구", 12);
        put("은평구", 12);
        put("중랑구", 12);
    }};

    public ResponseEntity<?> getAllRestaurantPlaces() {
        // 모든 구에 대해 데이터를 수집
        for (Map.Entry<String, Integer> entry : REQUIRED_DATA.entrySet()) {
            String district = entry.getKey();
            int requiredCount = entry.getValue();

            getRestaurantsForDistrict(district, requiredCount);
        }

        return new ResponseEntity<>("모든 구의 데이터 수집 완료", HttpStatus.OK);
    }
    private void getRestaurantsForDistrict(String district, int requiredCount) {
        String pageToken = null;
        List<RestaurantResponseDto.PlaceDetailResult> allRestaurants = new ArrayList<>();

        do {
            GooglePlacesAPIResponseDto response = googlePlaceIdClient.getTopRestaurants(
                    district + " 음식점",
                    "restaurant",
                    "prominence",
                    apiKey,
                    "ko",
                    pageToken
            );

            if (!Objects.equals(response.getStatus(), "OK")) {
                log.error("API 호출 실패: 구 = {}, 메시지 = {}", district, response.getStatus());
                break;
            }

            List<RestaurantResponseDto.PlaceDetailResult> currentResults = response.getResults().stream()
                    .filter(result -> result.getFormatted_address().contains("서울"))
                    .map(result -> {
                        RestaurantResponseDto.PlaceDetailResult dto = new RestaurantResponseDto.PlaceDetailResult();
                        dto.setFormattedAddress(result.getFormatted_address());
                        dto.setName(result.getName());
                        dto.setLatitude(result.getGeometry().getLocation().getLat());
                        dto.setLongitude(result.getGeometry().getLocation().getLng());
                        dto.setPlaceId(result.getPlace_id());
                        dto.setPriceLevel(result.getPrice_level());
                        dto.setReference(result.getReference());
                        dto.setRating(result.getRating());
                        dto.setUserRatingsTotal(result.getUser_ratings_total());
                        return dto;
                    }).toList();

            allRestaurants.addAll(currentResults);

            // 다음 페이지 토큰 갱신
            pageToken = response.getNext_page_token();

            // API 사용 제한 때문에 지연을 추가
            try {
                if (pageToken != null) {
                    Thread.sleep(2000); // 2초 지연
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        } while (allRestaurants.size() < requiredCount && pageToken != null);

        log.info("구: {}에서 수집된 데이터 수: {}", district, allRestaurants.size());

        // 필요한 데이터 수만큼 수집한 경우에만 저장
        if (allRestaurants.size() >= requiredCount) {
            RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();
            restaurantResponseDto.setPlaceDetailResults(allRestaurants);
            findRestaurantService.saveAllRestaurants(restaurantResponseDto);
        }
    }
}