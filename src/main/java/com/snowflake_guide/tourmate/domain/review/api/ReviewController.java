package com.snowflake_guide.tourmate.domain.review.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowflake_guide.tourmate.domain.review.dto.ReviewRequestDto;
import com.snowflake_guide.tourmate.domain.review.dto.ValidationErrorResponse;
import com.snowflake_guide.tourmate.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Review API", description = "테마별 리뷰를 조회, 등록, 삭제하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Operation(summary = "리뷰 등록", description = "지정된 방문지에 리뷰를 작성합니다.")
    @PostMapping(value = "/{visitedPlaceId}/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(
            @Parameter(description = "방문지 ID", example = "1") @PathVariable("visitedPlaceId") Long visitedPlaceId,
            @Parameter(description = "리뷰 내용 (reviewDec), 별점(rate)") @RequestPart(value = "reviewRequestDto", required = false) String reviewRequestDtoJson,
            @Parameter(description = "리뷰 이미지 파일들 (최대 3개)") @RequestPart(value = "reviewImages", required = false) MultipartFile[] reviewImages
    ) throws IOException {
        if (reviewRequestDtoJson == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "reivew_create_empty_dto", "message", "리뷰 등록 요청 dto가 비었습니다."));
        }

        ReviewRequestDto reviewRequestDto = objectMapper.readValue(reviewRequestDtoJson, ReviewRequestDto.class);

        // 유효성 검사 수행
        Set<ConstraintViolation<ReviewRequestDto>> violations = validator.validate(reviewRequestDto);
        if (!violations.isEmpty()) {
            // 유효성 검사 실패 시, 오류 목록 생성
            List<ValidationErrorResponse.FieldError> fieldErrors = violations.stream()
                    .map(violation -> new ValidationErrorResponse.FieldError(violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(Collectors.toList());
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(fieldErrors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // 유효성 검사 통과 시, 리뷰 생성 로직 실행
        reviewService.createReview(reviewRequestDto, visitedPlaceId, reviewImages);
        return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
    }
}