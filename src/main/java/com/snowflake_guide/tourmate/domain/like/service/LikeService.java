package com.snowflake_guide.tourmate.domain.like.service;

import com.snowflake_guide.tourmate.domain.like.dto.LikeListResponseDto;
import com.snowflake_guide.tourmate.domain.like.entity.Like;
import com.snowflake_guide.tourmate.domain.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

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
}
