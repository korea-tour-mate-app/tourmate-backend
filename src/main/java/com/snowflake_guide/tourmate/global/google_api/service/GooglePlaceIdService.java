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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlaceIdService {
    private final GooglePlaceIdClient googlePlaceIdClient;
    private final FindRestaurantService findRestaurantService;

    @Value("${google.api.key}")
    private String apiKey;

    public ResponseEntity<?> getTopRestaurantPlaces() {
        // Google Places API 호출
        GooglePlacesAPIResponseDto topRestaurants = googlePlaceIdClient.getTopRestaurants(
                "서울 음식점", // 검색어
                "restaurant", // 장소 타입
                "prominence", // 정렬 기준
                apiKey,
                "ko" // 한국어로 결과 요청
        );

        // 응답이 성공적이지 않을 경우 에러 메시지 반환
        if (!Objects.equals(topRestaurants.getStatus(), "OK")) {
            return new ResponseEntity<>("응답이 성공되지 못했습니다.", HttpStatus.OK);
        }

        // GoogleRestaurantResponseDto 객체 생성
        RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();
        List<RestaurantResponseDto.PlaceDetailResult> placeDetailResults = new ArrayList<>();

        // 응답을 변환해서 placeDetailResults에 저장
        topRestaurants.getResults().forEach(result -> {
            RestaurantResponseDto.PlaceDetailResult dto = new RestaurantResponseDto.PlaceDetailResult();
            dto.setFormattedAddress(result.getFormatted_address());
            if (result.getFormatted_address().contains("서울")) {
                // 서울에 있는 경우에만 리스트에 추가
                dto.setName(result.getName());
                dto.setLatitude(result.getGeometry().getLocation().getLat());
                dto.setLongitude(result.getGeometry().getLocation().getLng());
                dto.setPlaceId(result.getPlace_id());
                dto.setPriceLevel(result.getPrice_level());
                dto.setReference(result.getReference());
                dto.setRating(result.getRating());
                dto.setUserRatingsTotal(result.getUser_ratings_total());

                // dto에 저장
                placeDetailResults.add(dto);

                log.info("Restaurant DTO: {}", dto.toString());
            } else {
                log.info("서울이 아닌 주소이므로 저장을 하지 않습니다.: {}", dto.getFormattedAddress());
            }
        });

        log.info("현재까지 저장된 DTO 개수: {}", placeDetailResults.size());

        // placeDetailResults와 next_page_token 설정
        restaurantResponseDto.setPlaceDetailResults(placeDetailResults);
        restaurantResponseDto.setNext_page_token(topRestaurants.getNext_page_token());
        log.info("next token은?: {}", topRestaurants.getNext_page_token());


        // 모아둔 레스토랑 리스트를 한 번에 저장
        findRestaurantService.saveAllRestaurants(restaurantResponseDto);

        // 응답 반환
        return new ResponseEntity<>(restaurantResponseDto, HttpStatus.OK);
    }
}