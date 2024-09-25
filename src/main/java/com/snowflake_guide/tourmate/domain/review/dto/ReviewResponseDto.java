package com.snowflake_guide.tourmate.domain.review.dto;

import com.snowflake_guide.tourmate.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String reviewDec;
    private Float rate;
    private String reviewUrl1;
    private String reviewUrl2;
    private String reviewUrl3;
    private boolean isMyReview; // 내 리뷰 여부 추가

    public ReviewResponseDto(Review review, boolean isMyReview) {
        this.reviewId = review.getReviewId();
        this.reviewDec = review.getReviewDec();
        this.rate = review.getRate();
        this.reviewUrl1 = review.getReviewUrl1();
        this.reviewUrl2 = review.getReviewUrl2();
        this.reviewUrl3 = review.getReviewUrl3();
        this.isMyReview = isMyReview;
    }
}
