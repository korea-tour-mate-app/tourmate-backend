package com.snowflake_guide.tourmate.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    @NotBlank(message = "리뷰 내용은 필수항목입니다.")
    private String reviewDec;

    @NotNull(message = "평점은 필수항목입니다.")
    @Min(value = 0, message = "평점은 최소 0 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5 이하여야 합니다.")
    private Float rate;
}