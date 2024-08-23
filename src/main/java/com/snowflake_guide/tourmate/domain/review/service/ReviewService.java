package com.snowflake_guide.tourmate.domain.review.service;

import com.snowflake_guide.tourmate.domain.review.dto.ReviewRequestDto;
import com.snowflake_guide.tourmate.domain.review.entity.Review;
import com.snowflake_guide.tourmate.domain.review.repository.ReviewRepository;
import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import com.snowflake_guide.tourmate.domain.visited_place.repository.VisitedPlaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final VisitedPlaceRepository visitedPlaceRepository;
    private final S3Service s3Service;
    @Transactional
    public void createReview(ReviewRequestDto reviewRequestDto, Long visitedPlaceId, MultipartFile[] reviewImages) throws IOException {
        // VisitedPlace 존재 여부 확인
        Optional<VisitedPlace> visitedPlaceOpt = visitedPlaceRepository.findById(visitedPlaceId);
        if (visitedPlaceOpt.isEmpty()) {
            throw new IllegalArgumentException("해당 방문한 장소가 존재하지 않습니다.");
        }
        VisitedPlace visitedPlace = visitedPlaceOpt.get();


        // S3에 이미지 업로드
        List<String> imageUrls = new ArrayList<>();
        if (reviewImages != null) {
            for (MultipartFile image : reviewImages) {
                if (!image.isEmpty()) {
                    String imageUrl = s3Service.upload("reviews", image.getOriginalFilename(), image);
                    imageUrls.add(imageUrl);
                }
                if (imageUrls.size() == 3) break;  // 최대 3장까지 업로드
            }
        }

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
}
