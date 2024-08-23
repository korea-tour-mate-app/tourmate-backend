package com.snowflake_guide.tourmate.domain.review.repository;

import com.snowflake_guide.tourmate.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
