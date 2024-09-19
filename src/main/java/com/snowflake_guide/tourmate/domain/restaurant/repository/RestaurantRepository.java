package com.snowflake_guide.tourmate.domain.restaurant.repository;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findByPlaceId(String placeId); // placeId로 레스토랑 찾기
}