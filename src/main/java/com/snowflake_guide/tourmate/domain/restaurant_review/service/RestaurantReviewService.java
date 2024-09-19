package com.snowflake_guide.tourmate.domain.restaurant_review.service;

import com.snowflake_guide.tourmate.domain.restaurant_review.entity.RestaurantReview;
import com.snowflake_guide.tourmate.domain.restaurant_review.repository.RestaurantReviewRepository;
import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantReviewService {
    private final RestaurantReviewRepository restaurantReviewRepository;
    private final RestaurantRepository restaurantRepository;
    public void saveReviews(Restaurant restaurant, RestaurantReviewResponseDto restaurantReviewResponseDto) {
        if (restaurantReviewResponseDto.getFormatted_phone_number() != null){
            // 전화번호 설정
            restaurant.setFormattedPhoneNumber(restaurantReviewResponseDto.getFormatted_phone_number());
        }
        if (restaurantReviewResponseDto.getWeekday_text() != null){
            // weekdayText 리스트를 하나의 문자열로 변환
            String weekdayText = String.join(", ", restaurantReviewResponseDto.getWeekday_text());
            restaurant.setWeekdayText(weekdayText);
        }

        // 수정된 Restaurant 엔티티 저장
        restaurantRepository.save(restaurant);

        List<RestaurantReview> restaurantsReviews = new ArrayList<>();

        restaurantReviewResponseDto.getReviews().forEach(reviewDto -> {
            RestaurantReview restaurantReview = RestaurantReview.builder()
                    .authorName(reviewDto.getAuthor_name())
                    .language(reviewDto.getLanguage())
                    .rating(reviewDto.getRating())
                    .relativeTimeDescription(reviewDto.getRelative_time_description())
                    .text(reviewDto.getText())
                    .time(reviewDto.getTime())
                    .restaurant(restaurant)
                    .build();

            restaurantsReviews.add(restaurantReview); // 리스트에 리뷰 추가
        });

        // 리뷰 리스트를 한 번에 저장
        restaurantReviewRepository.saveAll(restaurantsReviews);
    }
}
