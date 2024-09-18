package com.snowflake_guide.tourmate.domain.RestaurantReview.repository;

import com.snowflake_guide.tourmate.domain.RestaurantReview.entity.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
}