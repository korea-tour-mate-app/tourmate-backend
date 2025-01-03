package com.snowflake_guide.tourmate.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    @Schema(description = "리뷰 내용", example = "경관이 좋은 장소입니다!")
    @NotBlank(message = "리뷰 내용은 필수항목입니다.")
    private String reviewDec;

    @Schema(description = "평점", example = "4.5")
    @NotNull(message = "Rate는 필수 항목입니다.")
    @Min(value = 0, message = "Rate는 0 이상이어야 합니다.")
    @Max(value = 5, message = "Rate는 5 이하이어야 합니다.")
    private Float rate;
}