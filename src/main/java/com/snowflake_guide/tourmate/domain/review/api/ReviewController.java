package com.snowflake_guide.tourmate.domain.review.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowflake_guide.tourmate.domain.review.dto.ReviewRequestDto;
import com.snowflake_guide.tourmate.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Review API", description = "테마별 리뷰를 조회, 등록, 삭제하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "지정된 방문지에 리뷰를 작성합니다.")
    @PostMapping(value = "/{visitedPlaceId}/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createReview(
            @Parameter(description = "방문지 ID", example = "1") @PathVariable("visitedPlaceId") Long visitedPlaceId,
            @Parameter(description = "리뷰 내용 (reviewDec), 별점(rate)") @RequestPart("reivew") String reviewRequestDtoJson,
            @Parameter(description = "리뷰 이미지 파일들 (최대 3개)") @RequestPart(value = "reviewImages", required = false) MultipartFile[] reviewImages
    ) throws IOException {
        ReviewRequestDto reviewRequestDto = new ObjectMapper().readValue(reviewRequestDtoJson, ReviewRequestDto.class);
        validateAndCreateReview(reviewRequestDto, visitedPlaceId, reviewImages);
    }


    private void validateAndCreateReview(@Valid ReviewRequestDto reviewRequestDto, Long visitedPlaceId, MultipartFile[] reviewImages) throws IOException {
        reviewService.createReview(reviewRequestDto, visitedPlaceId, reviewImages);
    }
}
