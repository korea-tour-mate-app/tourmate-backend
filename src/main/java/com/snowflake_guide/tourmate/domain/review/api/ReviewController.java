package com.snowflake_guide.tourmate.domain.review.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowflake_guide.tourmate.domain.review.dto.ReviewRequestDto;
import com.snowflake_guide.tourmate.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Review API", description = "테마별 리뷰를 조회, 등록, 삭제하는 api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    // TODO 로그인 구현 후 회원 정보 가져오는 부분 수정
    @PostMapping("/theme/{visitedPlaceId}/review")
    public void createReview(@PathVariable("visitedPlaceId") Long visitedPlaceId,
                             @RequestPart("post") String reviewRequestDtoJson,
                             @RequestPart(value = "reviewImages", required = false) MultipartFile[] reviewImages)
            throws IOException {

        ReviewRequestDto reviewRequestDto = new ObjectMapper().readValue(reviewRequestDtoJson, ReviewRequestDto.class);
        validateAndCreateReview(reviewRequestDto, visitedPlaceId, reviewImages);
    }

    private void validateAndCreateReview(@Valid ReviewRequestDto reviewRequestDto, Long visitedPlaceId, MultipartFile[] reviewImages) throws IOException {
        reviewService.createReview(reviewRequestDto, visitedPlaceId, reviewImages);
    }
}
