package com.snowflake_guide.tourmate.domain.like.service;

import com.snowflake_guide.tourmate.domain.like.dto.LikeListResponseDto;
import com.snowflake_guide.tourmate.domain.like.entity.Like;
import com.snowflake_guide.tourmate.domain.like.repository.LikeRepository;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<LikeListResponseDto> getLikedPlaces(Long memberId, Long themeId) {
        List<Like> likes;

        if (themeId != null) {
            // Member -> Like -> Place -> Theme
            // themeId가 제공된 경우, 해당 테마에 속하는 장소 중 좋아요한 것들만 필터링
            likes = likeRepository.findByMember_MemberIdAndPlace_Theme_ThemeId(memberId, themeId);
        } else {
            // themeId가 제공되지 않은 경우, 모든 좋아요한 장소 반환
            likes = likeRepository.findByMember_MemberId(memberId);
        }

        return likes.stream()
                .map(like -> new LikeListResponseDto(like.getPlace()))
                .collect(Collectors.toList());
    }

    /***
     * Like 엔티티가 존재하고 liked가 true인 경우: liked를 false로 변경
     * Like 엔티티가 존재하고 liked가 false인 경우: liked를 true로 변경
     * Like 엔티티가 존재하지 않는 경우: 새로운 Like 엔티티를 생성하고 liked를 true로 설정
     *
     * @param memberId
     * @param placeId
     */
    @Transactional
    public void toggleLike(Long memberId, Long placeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));

        Optional<Like> existingLikeOpt = likeRepository.findByMember_MemberIdAndPlace_PlaceId(memberId, placeId);

        if (existingLikeOpt.isPresent()) {
            Like existingLike = existingLikeOpt.get();
            // 좋아요 상태를 토글
            existingLike.toggleLiked(); // 좋아요 상태를 토글
            likeRepository.save(existingLike);
        } else {
            // 새로운 좋아요 생성
            Like newLike = Like.builder()
                    .member(member)
                    .place(place)
                    .liked(true)
                    .build();
            likeRepository.save(newLike);
        }
    }
}
