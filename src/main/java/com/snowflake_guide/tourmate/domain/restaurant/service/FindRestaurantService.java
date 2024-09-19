package com.snowflake_guide.tourmate.domain.restaurant.service;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.domain.restaurant_review.dto.FindRestaurantsRequestDto;
import com.snowflake_guide.tourmate.domain.restaurant_review.dto.FindRestaurantsResponseDto;
import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindRestaurantService {
    private final RestaurantRepository restaurantRepository;

    // [초기세팅] 서울에 있는 인기 있는 500개의 음식점 저장
    @Transactional
    public void saveAllRestaurants(RestaurantResponseDto restaurantResponseDto) {
        List<RestaurantResponseDto.PlaceDetailResult> list = restaurantResponseDto.getPlaceDetailResults();
        String nextPageToken = restaurantResponseDto.getNext_page_token();
        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            RestaurantResponseDto.PlaceDetailResult result = list.get(i);

            // 첫 번째 PlaceDetailResult인 경우에만 nextPageToken 설정
            Restaurant.RestaurantBuilder builder = Restaurant.builder()
                    .name(result.getName())
                    .formattedAddress(result.getFormattedAddress())
                    .latitude(result.getLatitude())
                    .longitude(result.getLongitude())
                    .placeId(result.getPlaceId())
                    .priceLevel(result.getPriceLevel())
                    .reference(result.getReference())
                    .userRatingsTotal(result.getUserRatingsTotal())
                    .totalRating(result.getRating());

            if (i == 0) { // 첫 번째 요소인 경우 nextPageToken 추가
                builder.nextPageToken(nextPageToken);
            }
            Restaurant restaurant = builder.build();
            restaurants.add(restaurant); // 리스트에 레스토랑 추가
        }

        // Restaurant 엔티티 리스트를 한꺼번에 저장
        restaurantRepository.saveAll(restaurants);
    }

    // 특정 장소 주변에 있는 음식점 3개 조회
    public FindRestaurantsResponseDto getRestaurantPlaceId(FindRestaurantsRequestDto findRestaurantsRequestDto) {
        List<FindRestaurantsResponseDto.LocationWithRestaurants> results = new ArrayList<>();
        Set<Long> excludedRestaurantIds = new HashSet<>(); // 중복을 방지하기 위한 Set

        // 사용자가 입력한 각 위치마다 반복
        for (FindRestaurantsRequestDto.LocationRequestDto location : findRestaurantsRequestDto.getPlace_locations()) {
            // 사각형 범위 계산
            double[] boundingBox = calculateBoundingBox(location.getPlace_latitude(), location.getPlace_longitude(), 5.0);

            // 현재 위치에서 가장 가까운 3개의 음식점 찾기
            List<Restaurant> top3Restaurants = restaurantRepository.findNearestRestaurants(
                    location.getPlace_latitude(),
                    location.getPlace_longitude(),
                    boundingBox[0], // minLatitude
                    boundingBox[1], // maxLatitude
                    boundingBox[2], // minLongitude
                    boundingBox[3], // maxLongitude
                    excludedRestaurantIds,
                    PageRequest.of(0, 3)
            );

            // 현재 위치의 음식점 목록을 DTO로 변환
            List<FindRestaurantsResponseDto.RestaurantInfo> restaurantInfos = top3Restaurants.stream()
                    .map(restaurant -> FindRestaurantsResponseDto.RestaurantInfo.builder()
                            .restaurantId(restaurant.getRestaurantId())
                            .res_name(restaurant.getName())
                            .res_latitude(restaurant.getLatitude())
                            .res_longitude(restaurant.getLongitude())
                            .build())
                    .collect(Collectors.toList());

            // 찾은 음식점의 ID를 중복 방지 Set에 추가 -> 다음의 place때 해당 음식점은 포함시키지 못하도록 함
            excludedRestaurantIds.addAll(
                    top3Restaurants.stream()
                            .map(Restaurant::getRestaurantId)
                            .collect(Collectors.toSet())
            );

            // 현재 위치와 그 위치의 음식점 정보를 결과 리스트에 추가
            results.add(new FindRestaurantsResponseDto.LocationWithRestaurants(location, restaurantInfos));
        }

        // 최종 결과 반환
        return new FindRestaurantsResponseDto(results);
    }
    // 특정 중심점(위도와 경도)과 반경(거리)을 기준으로 사각형 범위(경계)를 계산
    public double[] calculateBoundingBox(double latitude, double longitude, double distanceInKm) {
        // 지구 반지름 (단위: km)
        final double R = 6371;

        // 각도 단위로 거리 변환
        double latDistance = distanceInKm / R;
        double lonDistance = distanceInKm / (R * Math.cos(Math.toRadians(latitude)));

        double minLatitude = latitude - Math.toDegrees(latDistance);
        double maxLatitude = latitude + Math.toDegrees(latDistance);
        double minLongitude = longitude - Math.toDegrees(lonDistance);
        double maxLongitude = longitude + Math.toDegrees(lonDistance);

        return new double[]{minLatitude, maxLatitude, minLongitude, maxLongitude};
    }

}
