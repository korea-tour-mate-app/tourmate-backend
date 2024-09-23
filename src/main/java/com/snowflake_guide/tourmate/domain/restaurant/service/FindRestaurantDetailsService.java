package com.snowflake_guide.tourmate.domain.restaurant.service;

import com.snowflake_guide.tourmate.domain.restaurant.dto.FindRestaurantDetailsResponseDto;
import com.snowflake_guide.tourmate.domain.restaurant.entity.Restaurant;
import com.snowflake_guide.tourmate.domain.restaurant.repository.RestaurantRepository;
import com.snowflake_guide.tourmate.domain.restaurant_review.entity.RestaurantReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindRestaurantDetailsService {
    private final RestaurantRepository restaurantRepository;

    public FindRestaurantDetailsResponseDto getRestaurantDetails(Long restaurantId) {
        // restaurantId로 레스토랑 및 리뷰 정보 조회
        Restaurant restaurant = restaurantRepository.findByRestaurantIdWithReviews(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + restaurantId));

        // 리뷰 리스트를 DTO로 변환
        List<FindRestaurantDetailsResponseDto.Review> reviewDtos = restaurant.getReviews().stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList());

        // 레스토랑 정보와 리뷰 정보를 포함하는 DTO 생성 및 반환
        return FindRestaurantDetailsResponseDto.builder()
                .restaurantId(restaurantId)
                .restaurantName(restaurant.getName())
                .priceLevel(restaurant.getPriceLevel())
                .totalRating(restaurant.getTotalRating())
                .userRatingsTotal(restaurant.getUserRatingsTotal())
                .formattedPhoneNumber(restaurant.getFormattedPhoneNumber())
                .weekdayText(restaurant.getWeekdayText())
                .reviews(reviewDtos) // DTO로 변환된 리뷰 리스트 설정
                .build();
    }

    // RestaurantReview를 FindRestaurantDetailsResponseDto.Review로 변환하는 메서드
    private FindRestaurantDetailsResponseDto.Review convertToReviewDto(RestaurantReview review) {
        return FindRestaurantDetailsResponseDto.Review.builder()
                .author_name(review.getAuthorName())
                .language(review.getLanguage())
                .rating(review.getReviewRating())
                .relative_time_description(review.getRelativeTimeDescription())
                .text(review.getText())
                .time(review.getTime())
                .build();
    }
}
