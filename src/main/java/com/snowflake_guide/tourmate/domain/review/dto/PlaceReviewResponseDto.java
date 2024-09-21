package com.snowflake_guide.tourmate.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceReviewResponseDto {
    private List<ReviewResponseDto> reviews;
    private boolean visited;
}

