package com.snowflake_guide.tourmate.domain.restaurant.repository;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}