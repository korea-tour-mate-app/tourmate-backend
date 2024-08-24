package com.snowflake_guide.tourmate.domain.visited_place.repository;

import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitedPlaceRepository extends JpaRepository<VisitedPlace, Long> {
    List<VisitedPlace> findByPlace_PlaceId(Long placeId);

    // memberId의 회원이 방문했던 장소 리스트
    List<VisitedPlace> findByMember_MemberIdAndVisitedTrue(Long memberId);

    // memberId의 회원이 특정 테마에 방문했던 장소 리스트
    List<VisitedPlace> findByMember_MemberIdAndPlace_Theme_ThemeIdAndVisitedTrue(Long memberId, Long themeId);

    // MyPlace ID와 Place ID로 VisitedPlace를 조회
    Optional<VisitedPlace> findByMember_MemberIdAndPlace_PlaceId(Long memberId, Long placeId);
}
