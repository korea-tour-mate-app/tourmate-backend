package com.snowflake_guide.tourmate.domain.review.service;

import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.domain.review.dto.PlaceReviewResponseDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final VisitedPlaceRepository visitedPlaceRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;

    public void createReview(String email, ReviewRequestDto reviewRequestDto, Long placeId, MultipartFile[] reviewImages) {
        // S3에 이미지 업로드 (트랜잭션 외부에서 처리)
        List<String> imageUrls = new ArrayList<>();
        if (reviewImages != null) {
            try {
                for (MultipartFile image : reviewImages) {
                    if (!image.isEmpty()) {
                        String imageUrl = s3Service.upload("reviews", image.getOriginalFilename(), image); // reviews 폴더에 저장
                        imageUrls.add(imageUrl);
                        log.info("리뷰 이미지 업로드 성공: {}", imageUrl);
                    }
                    if (imageUrls.size() == 3) break;  // 최대 3장까지 업로드
                }
            } catch (IOException e) {
                log.error("리뷰 이미지 업로드 중 오류 발생: {}", e.getMessage());
                throw new RuntimeException("리뷰 이미지 업로드 중 오류가 발생했습니다.", e); // 이 예외는 컨트롤러에서 처리하도록 던집니다.
            }
        }

        // 리뷰 저장 트랜잭션 시작
        saveReview(email, reviewRequestDto, placeId, imageUrls);
    }

    @Transactional
    public void saveReview(String email, ReviewRequestDto reviewRequestDto, Long placeId, List<String> imageUrls) {
        // 인증객체가 올바른지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        VisitedPlace visitedPlace = visitedPlaceRepository.findByPlace_PlaceIdAndMember_Email(placeId, email)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소에 대한 방문 기록이 존재하지 않습니다."));
        Long visitedPlaceId = visitedPlace.getVisitedPlaceId();
//        // VisitedPlace 존재 여부 확인
//        VisitedPlace visitedPlace = visitedPlaceRepository.findById(visitedPlaceId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 방문한 장소가 존재하지 않습니다."));

        // VisitedPlace가 현재 회원의 것인지 확인
        if (!visitedPlace.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("해당 방문한 장소는 현재 회원의 방문 기록이 아니므로 리뷰를 작성할 수 없습니다.");
        }
        log.info("리뷰를 저장할 방문지 확인: 방문지 ID = {}", visitedPlaceId);

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
        log.info("리뷰가 성공적으로 저장되었습니다: 리뷰 ID = {}, 방문지 ID = {}", review.getReviewId(), visitedPlaceId);
    }

    @Transactional
    public void deleteReview(String email, Long reviewId) {
        // 리뷰가 존재하는지 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));
        // 인증객체가 올바른지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        // 리뷰가 해당 사용자의 것인지 확인
        if (!review.getVisitedPlace().getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("해당 리뷰를 삭제할 권한이 없습니다.");
        }
        try {
            deleteReviewImages(review);
            log.info("리뷰 이미지가 성공적으로 삭제되었습니다: 리뷰 ID = {}", reviewId);
        } catch (RuntimeException e) {
            log.error("리뷰 이미지 삭제 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("리뷰 이미지 삭제 중 오류가 발생했습니다.", e);
        }

        reviewRepository.delete(review);
        log.info("리뷰가 성공적으로 삭제되었습니다: 리뷰 ID = {}", reviewId);
    }

    // S3에서 이미지 삭제 진행
    private void deleteReviewImages(Review review) {
        if (review.getReviewUrl1() != null) s3Service.delete(review.getReviewUrl1());
        if (review.getReviewUrl2() != null) s3Service.delete(review.getReviewUrl2());
        if (review.getReviewUrl3() != null) s3Service.delete(review.getReviewUrl3());
    }

    @Transactional(readOnly = true)
    public PlaceReviewResponseDto getReviewsByPlaceId(Long placeId, String email) {
        // 인증객체가 올바른지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        Long memberId = member.getMemberId();

        // Place ID에 해당하는 VisitedPlace 목록을 가져옴
        List<VisitedPlace> visitedPlaces = visitedPlaceRepository.findByPlace_PlaceId(placeId);

        // VisitedPlace가 있는지 확인하고, 해당 VisitedPlace가 현재 회원(memberId)과 일치하는지 확인
        boolean isVisited = visitedPlaces.stream()
                .anyMatch(visitedPlace -> visitedPlace.getMember().getMemberId().equals(memberId) && visitedPlace.getVisited());
        log.info("회원의 방문 여부 확인: 방문지 ID = {}, 방문 여부 = {}", placeId, isVisited);

        // 리뷰 리스트 생성
        List<ReviewResponseDto> reviewList = visitedPlaces.stream()
                .flatMap(visitedPlace -> visitedPlace.getReviews().stream())
                .map(review -> {
                    boolean isMyReview = review.getVisitedPlace().getMember().getMemberId().equals(memberId);
                    return new ReviewResponseDto(review, isMyReview);
                })
                .toList();
        log.info("리뷰 조회 완료: 장소 ID = {}, 리뷰 수 = {}", placeId, reviewList.size());
        // 리뷰 리스트와 방문 여부를 포함한 응답 생성
        return new PlaceReviewResponseDto(reviewList, isVisited);
    }
}