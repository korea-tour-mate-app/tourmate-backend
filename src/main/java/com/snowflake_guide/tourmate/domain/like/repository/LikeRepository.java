package com.snowflake_guide.tourmate.domain.like.repository;

import com.snowflake_guide.tourmate.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // 특정 회원이 좋아요한 모든 장소를 조회
    List<Like> findByMember_MemberId(Long memberId);

    // 특정 회원이 특정 테마에 속한 장소에 대해 좋아요한 기록을 조회
    List<Like> findByMember_MemberIdAndPlace_Theme_ThemeId(Long memberId, Long themeId);

    // 특정 회원이 특정 장소에 대해 좋아요한 기록을 조회 (회원과 장소 사이에 하나의 좋아요만 존재)
    Optional<Like> findByMember_MemberIdAndPlace_PlaceId(Long memberId, Long placeId);
}
