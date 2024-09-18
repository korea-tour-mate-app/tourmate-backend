package com.snowflake_guide.tourmate.global.google_api.service;

import com.snowflake_guide.tourmate.domain.RestaurantReview.service.RestaurantReviewService;
import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.global.client.GooglePlaceReviewClient;
import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlaceDetailsResponseDto;
import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDetailsService {
    private final RestaurantRepository restaurantRepository;
    private final GooglePlaceReviewClient googlePlaceReviewClient;
    private final RestaurantReviewService restaurantReviewService;

    @Value("${google.api.key}")
    private String apiKey;

    public RestaurantReviewResponseDto getRestaurantReviews(String placeId) {
        // 해당 placeId로 Restaurant 정보 가져오기
        Restaurant restaurant = restaurantRepository.findByPlaceId(placeId);

        if (Objects.isNull(restaurant)) {
            throw new RuntimeException("Restaurant not found with placeId: " + placeId);
        }

        // Google Places API를 사용하여 리뷰 정보 가져오기
        GooglePlaceDetailsResponseDto responseDto = googlePlaceReviewClient.getPlaceReviews(placeId, apiKey);

        // 리뷰 정보를 변환한 RestaurantReviewResponseDto 반환
        RestaurantReviewResponseDto restaurantReviewResponseDto = new RestaurantReviewResponseDto();
        restaurantReviewResponseDto.setFormatted_phone_number(responseDto.getResult().getFormatted_phone_number());
        restaurantReviewResponseDto.setWeekday_text(responseDto.getResult().getOpening_hours().getWeekday_text());

        List<GooglePlaceDetailsResponseDto.Review> reviews = responseDto.getResult().getReviews();
        reviews.forEach(review -> {
            RestaurantReviewResponseDto.Review reviewDto = new RestaurantReviewResponseDto.Review();
            reviewDto.setAuthor_name(review.getAuthor_name());
            reviewDto.setText(review.getText());
            reviewDto.setRating(review.getRating());
            reviewDto.setRelative_time_description(review.getRelative_time_description());
            reviewDto.setTime(review.getTime());
            reviewDto.setLanguage(review.getLanguage());
            reviewDto.setProfile_photo_url(review.getProfile_photo_url());

            // 리뷰 정보를 restaurantReviewResponseDto에 추가
            restaurantReviewResponseDto.getReviews().add(reviewDto);
        });

        log.info("RestaurantReviewResponseDto: {}", restaurantReviewResponseDto.toString());

        restaurantReviewService.saveReviews(restaurant, restaurantReviewResponseDto);
        // 변환된 리뷰 정보 및 전화번호, 영업시간 정보를 포함한 DTO 반환

        return restaurantReviewResponseDto;
    }
}
