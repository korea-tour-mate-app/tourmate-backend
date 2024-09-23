package com.snowflake_guide.tourmate.global.google_api.service;

import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.domain.restaurant_review.service.RestaurantReviewService;
import com.snowflake_guide.tourmate.global.client.GooglePlaceReviewClient;
import com.snowflake_guide.tourmate.global.google_api.dto.GooglePlaceDetailsResponseDto;
import com.snowflake_guide.tourmate.global.google_api.dto.RestaurantReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDetailsService {
    private final RestaurantRepository restaurantRepository;
    private final GooglePlaceReviewClient googlePlaceReviewClient;
    private final RestaurantReviewService restaurantReviewService;

    @Value("${google.api.key}")
    private String apiKey;

    public void getRestaurantReviews(int startId, int endId) {
        // restaurantId가 1부터 499까지의 레스토랑을 가져옵니다.
        for (long restaurantId = startId; restaurantId <= endId; restaurantId++) {
            try {
                // 레스토랑 엔티티 가져오기
                long finalRestaurantId = restaurantId;
                Restaurant restaurant = restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + finalRestaurantId));

                // Google Details API를 호출하여 리뷰 가져오기
                String placeId = restaurant.getPlaceId();
                GooglePlaceDetailsResponseDto responseDto = googlePlaceReviewClient.getPlaceReviews(placeId, apiKey, "ko");

                // 리뷰 정보를 변환한 RestaurantReviewResponseDto 반환
                RestaurantReviewResponseDto restaurantReviewResponseDto = getRestaurantReviewResponseDto(responseDto);

                log.info("RestaurantReviewResponseDto: {}", restaurantReviewResponseDto.toString());

                // 리뷰를 데이터베이스에 저장
                restaurantReviewService.saveReviews(restaurant, restaurantReviewResponseDto);

            } catch (Exception e) {
                log.error("Failed to fetch reviews for Restaurant ID {}: {}", restaurantId, e.getMessage());
            }
        }
    }

    // 리뷰 정보를 변환하여 RestaurantReviewResponseDto를 반환하는 메서드
    private RestaurantReviewResponseDto getRestaurantReviewResponseDto(GooglePlaceDetailsResponseDto responseDto) {
        RestaurantReviewResponseDto restaurantReviewResponseDto = new RestaurantReviewResponseDto();
        restaurantReviewResponseDto.setFormatted_phone_number(responseDto.getResult().getFormatted_phone_number());
        restaurantReviewResponseDto.setWeekday_text(responseDto.getResult().getOpening_hours().getWeekday_text());

        // 리뷰 리스트 초기화 확인 및 설정
        if (restaurantReviewResponseDto.getReviews() == null) {
            restaurantReviewResponseDto.setReviews(new ArrayList<>()); // 리스트 초기화
        }

        List<GooglePlaceDetailsResponseDto.Review> reviews = responseDto.getResult().getReviews();
        reviews.forEach(review -> {
            RestaurantReviewResponseDto.Review reviewDto = new RestaurantReviewResponseDto.Review();
            reviewDto.setAuthor_name(review.getAuthor_name());
            reviewDto.setText(review.getText());
            reviewDto.setRating(review.getRating());
            reviewDto.setRelative_time_description(review.getRelative_time_description());
            reviewDto.setTime(review.getTime());
            reviewDto.setLanguage(review.getLanguage());

            // 리뷰 정보를 restaurantReviewResponseDto에 추가
            restaurantReviewResponseDto.getReviews().add(reviewDto);
        });

        return restaurantReviewResponseDto;
    }
}
