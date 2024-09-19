package com.snowflake_guide.tourmate.domain.restaurant_review.repository;

import com.snowflake_guide.tourmate.domain.restaurant_review.entity.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
}