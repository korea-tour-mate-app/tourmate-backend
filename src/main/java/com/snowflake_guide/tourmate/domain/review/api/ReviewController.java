package com.snowflake_guide.tourmate.domain.review.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowflake_guide.tourmate.domain.review.dto.PlaceReviewResponseDto;
import com.snowflake_guide.tourmate.domain.review.dto.ReviewRequestDto;
import com.snowflake_guide.tourmate.domain.review.dto.ReviewResponseDto;
import com.snowflake_guide.tourmate.domain.review.dto.ValidationErrorResponse;
import com.snowflake_guide.tourmate.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    ) {
        if (reviewRequestDtoJson == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "review_create_empty_dto", "message", "리뷰 등록 요청 dto가 비었습니다."));
        }

        ReviewRequestDto reviewRequestDto;
        try {
            reviewRequestDto = objectMapper.readValue(reviewRequestDtoJson, ReviewRequestDto.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "json_parsing_error", "message", "리뷰 등록 요청의 JSON 파싱 중 오류가 발생했습니다.", "details", e.getMessage()));
        }

        // 유효성 검사 수행
        Set<ConstraintViolation<ReviewRequestDto>> violations = validator.validate(reviewRequestDto);
        if (!violations.isEmpty()) {
            List<ValidationErrorResponse.FieldError> fieldErrors = violations.stream()
                    .map(violation -> new ValidationErrorResponse.FieldError(violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(Collectors.toList());
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(fieldErrors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            // 유효성 검사 통과 시, 리뷰 생성 로직 실행
            reviewService.createReview(reviewRequestDto, visitedPlaceId, reviewImages);
            return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("리뷰 생성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "visited_place_not_found", "message", e.getMessage()));
        } catch (RuntimeException e) {
            log.error("리뷰 생성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "image_upload_error", "message", "리뷰 이미지 업로드 중 오류가 발생했습니다.", "details", e.getMessage()));
        }
    }

    @Operation(summary = "리뷰 삭제", description = "지정된 리뷰를 삭제합니다.")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @Parameter(description = "리뷰 ID", example = "1") @PathVariable("reviewId") Long reviewId) {
        try {
            // TODO 본인의 리뷰인 경우에만 삭제 진행
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("리뷰 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "review_not_found", "message", e.getMessage()));
        } catch (RuntimeException e) {
            log.error("리뷰 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "review_delete_error", "message", "리뷰 삭제 중 오류가 발생했습니다.", "details", e.getMessage()));
        }
    }

    @Operation(summary = "특정 장소에 대한 리뷰 조회", description = "특정 장소에 대한 리뷰를 조회하고, 현재 사용자가 방문한 장소인지 확인합니다.")
    @GetMapping("/place/{placeId}/reviews")
    public ResponseEntity<?> getReviewsByPlaceId(
            @Parameter(description = "장소 ID", example = "1") @PathVariable("placeId") Long placeId,
            @Parameter(description = "회원 ID", example = "1") @RequestParam("memberId") Long memberId) {

        try {
            PlaceReviewResponseDto response = reviewService.getReviewsByPlaceId(placeId, memberId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("리뷰 조회 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "reviews_not_found", "message", e.getMessage()));
        }
    }

}