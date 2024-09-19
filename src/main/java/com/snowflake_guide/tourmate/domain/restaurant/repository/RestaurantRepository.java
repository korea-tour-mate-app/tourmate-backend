package com.snowflake_guide.tourmate.domain.restaurant.repository;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findByPlaceId(String placeId); // placeId로 레스토랑 찾기

    @Query(value = "SELECT r FROM Restaurant r WHERE " +
            "r.latitude BETWEEN :minLatitude AND :maxLatitude " +
            "AND r.longitude BETWEEN :minLongitude AND :maxLongitude " +
            "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) " + // Haversine formula (구의 표면 위의 두 점 사이의 거리를 계산)
            "* cos(radians(r.longitude) - radians(:longitude)) + sin(radians(:latitude)) " +
            "* sin(radians(r.latitude)))) < 5 " + // 계산된 거리가 5km 이내인 음식점만 조회
            "AND r.restaurantId NOT IN :excludedRestaurantIds " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) " + // 다른 위치에서 선택된 음식점을 중복 x
            "* cos(radians(r.longitude) - radians(:longitude)) + sin(radians(:latitude)) " +
            "* sin(radians(r.latitude)))) ASC")
    List<Restaurant> findNearestRestaurants(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("minLatitude") double minLatitude,
            @Param("maxLatitude") double maxLatitude,
            @Param("minLongitude") double minLongitude,
            @Param("maxLongitude") double maxLongitude,
            @Param("excludedRestaurantIds") Set<Long> excludedRestaurantIds,
            Pageable pageable);

    // restaurantId로 레스토랑 정보 조회
    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.reviews WHERE r.restaurantId = :restaurantId")
    Optional<Restaurant> findByRestaurantIdWithReviews(@Param("restaurantId") Long restaurantId);
}