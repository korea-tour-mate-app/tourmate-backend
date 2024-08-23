package com.snowflake_guide.tourmate.domain.review.service;

import com.snowflake_guide.tourmate.domain.my_place.repository.MyPlaceRepository;
import com.snowflake_guide.tourmate.domain.review.dto.ReviewRequestDto;
import com.snowflake_guide.tourmate.domain.review.dto.ReviewResponseDto;
import com.snowflake_guide.tourmate.domain.review.entity.Review;
import com.snowflake_guide.tourmate.domain.review.repository.ReviewRepository;
import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import com.snowflake_guide.tourmate.domain.visited_place.repository.VisitedPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final VisitedPlaceRepository visitedPlaceRepository;
    private final MyPlaceRepository myPlaceRepository;
    private final S3Service s3Service;

    public void createReview(ReviewRequestDto reviewRequestDto, Long visitedPlaceId, MultipartFile[] reviewImages) {
        // S3에 이미지 업로드 (트랜잭션 외부에서 처리)
        List<String> imageUrls = new ArrayList<>();
        if (reviewImages != null) {
            try {
                for (MultipartFile image : reviewImages) {
                    if (!image.isEmpty()) {
                        String imageUrl = s3Service.upload("reviews", image.getOriginalFilename(), image); // reviews 폴더에 저장
                        imageUrls.add(imageUrl);
                    }
                    if (imageUrls.size() == 3) break;  // 최대 3장까지 업로드
                }
            } catch (IOException e) {
                log.error("리뷰 이미지 업로드 중 오류 발생: {}", e.getMessage());
                throw new RuntimeException("리뷰 이미지 업로드 중 오류가 발생했습니다.", e); // 이 예외는 컨트롤러에서 처리하도록 던집니다.
            }
        }

        // 리뷰 저장 트랜잭션 시작
        saveReview(reviewRequestDto, visitedPlaceId, imageUrls);
    }

    @Transactional
    public void saveReview(ReviewRequestDto reviewRequestDto, Long visitedPlaceId, List<String> imageUrls) {
        // VisitedPlace 존재 여부 확인
        VisitedPlace visitedPlace = visitedPlaceRepository.findById(visitedPlaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방문한 장소가 존재하지 않습니다."));

        // Review 엔티티 생성 및 저장
        Review review = Review.builder()
                .visitedPlace(visitedPlace)
                .reviewDec(reviewRequestDto.getReviewDec())
                .rate(reviewRequestDto.getRate())
                .reviewUrl1(!imageUrls.isEmpty() ? imageUrls.get(0) : null)
                .reviewUrl2(imageUrls.size() > 1 ? imageUrls.get(1) : null)
                .reviewUrl3(imageUrls.size() > 2 ? imageUrls.get(2) : null)
                .build();

        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        try {
            deleteReviewImages(review);
        } catch (RuntimeException e) {
            log.error("리뷰 이미지 삭제 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("리뷰 이미지 삭제 중 오류가 발생했습니다.", e);
        }

        reviewRepository.delete(review);
    }
    // S3에서 이미지 삭제 진행
    private void deleteReviewImages(Review review) {
        if (review.getReviewUrl1() != null) s3Service.delete(review.getReviewUrl1());
        if (review.getReviewUrl2() != null) s3Service.delete(review.getReviewUrl2());
        if (review.getReviewUrl3() != null) s3Service.delete(review.getReviewUrl3());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByPlaceId(Long placeId, Long memberId) {
        // Place ID에 해당하는 VisitedPlace 목록을 가져옴
        List<VisitedPlace> visitedPlaces = visitedPlaceRepository.findByPlace_PlaceId(placeId);

        // VisitedPlace(방문한 장소)가 있는지 확인 (VisitedPlace -> MyPlace -> Member 경로를 따라 확인)
        // MyPlace의 값이 True인지 확인
        boolean isVisited = visitedPlaces.stream()
                .anyMatch(visitedPlace -> visitedPlace.getMyPlace().getMember().getMemberId().equals(memberId) && visitedPlace.getMyPlace().getVisited());

        // 리뷰 리스트 생성
        return visitedPlaces.stream()
                .flatMap(visitedPlace -> visitedPlace.getReviews().stream())
                .map(review -> new ReviewResponseDto(review, isVisited))
                .collect(Collectors.toList());
    }
}