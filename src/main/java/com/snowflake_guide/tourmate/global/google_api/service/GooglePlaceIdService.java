package com.snowflake_guide.tourmate.global.google_api.service;

import com.snowflake_guide.tourmate.global.client.GooglePlaceIdClient;
import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlacesAPIRequestDto;
import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlacesAPIResponseDto;
import com.snowflake_guide.tourmate.global.google_api.dto.GoogleRestaurantResponseDto;
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

    @Value("${google.api.key}")
    private String apiKey;

    public ResponseEntity<?> getTopRestaurantPlaceIds(GooglePlacesAPIRequestDto requestDto) {
        // Google Places API 호출
        GooglePlacesAPIResponseDto topRestaurants = googlePlaceIdClient.getTopRestaurants(
                "서울 음식점", // query parameter
                "restaurant", // type
                "prominence", // rankby
                apiKey // API key
        );

        // 응답이 성공적이지 않을 경우 에러 메시지 반환
        if (!Objects.equals(topRestaurants.getStatus(), "OK")) {
            return new ResponseEntity<>("응답이 성공되지 못했습니다.", HttpStatus.OK);
        }

        // GoogleRestaurantResponseDto 객체 생성
        GoogleRestaurantResponseDto googleRestaurantResponseDto = new GoogleRestaurantResponseDto();
        List<GoogleRestaurantResponseDto.PlaceDetailResult> placeDetailResults = new ArrayList<>();

        // 응답을 변환해서 placeDetailResults에 저장
        topRestaurants.getResults().forEach(result -> {
            GoogleRestaurantResponseDto.PlaceDetailResult dto = new GoogleRestaurantResponseDto.PlaceDetailResult();
            dto.setName(result.getName());
            dto.setFormattedAddress(result.getFormatted_address());
            dto.setLatitude(result.getGeometry().getLocation().getLat());
            dto.setLongitude(result.getGeometry().getLocation().getLng());

            // opening_hours가 null이 아닌 경우에만 처리
            if (result.getOpening_hours() != null) {
                dto.setOpenNow(result.getOpening_hours().isOpen_now());
            } else {
                dto.setOpenNow(false); // default로 영업 중이 아님
            }

            // 사진 참조값 리스트 설정
            if (result.getPhotos() != null) {
                List<String> photoReferences = new ArrayList<>();
                result.getPhotos().forEach(photo -> photoReferences.add(photo.getPhoto_reference()));
                dto.setPhotoReferences(photoReferences);
            }

            dto.setPlaceId(result.getPlace_id());
            dto.setPriceLevel(result.getPrice_level());
            dto.setReference(result.getReference());
            dto.setUserRatingsTotal(result.getUser_ratings_total());

            placeDetailResults.add(dto);
        });

        // placeDetailResults와 next_page_token 설정
        googleRestaurantResponseDto.setPlaceDetailResults(placeDetailResults);
        googleRestaurantResponseDto.setNext_page_token(topRestaurants.getNext_page_token());

        // 응답 반환
        return new ResponseEntity<>(googleRestaurantResponseDto, HttpStatus.OK);
    }
}