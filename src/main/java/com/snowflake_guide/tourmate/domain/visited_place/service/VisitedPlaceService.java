package com.snowflake_guide.tourmate.domain.visited_place.service;

import com.snowflake_guide.tourmate.domain.like.dto.LikeListResponseDto;
import com.snowflake_guide.tourmate.domain.like.entity.Like;
import com.snowflake_guide.tourmate.domain.like.repository.LikeRepository;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.domain.my_place.entity.MyPlace;
import com.snowflake_guide.tourmate.domain.my_place.repository.MyPlaceRepository;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.domain.place.repository.PlaceRepository;
import com.snowflake_guide.tourmate.domain.visited_place.dto.VisitedPlaceListResponseDto;
import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import com.snowflake_guide.tourmate.domain.visited_place.repository.VisitedPlaceRepository;
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
public class VisitedPlaceService {
    private final VisitedPlaceRepository visitedPlaceRepository;
    private final MyPlaceRepository myPlaceRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<VisitedPlaceListResponseDto> getVisitedPlaces(Long memberId, Long themeId) {
        List<VisitedPlace> visitedPlaces;

        if (themeId != null) {
            // Member -> MyPlace -> VisitedPlace -> Place -> Theme
            // themeId가 제공된 경우, 해당 테마에 속하는 장소 중 방문한 것들만 필터링
            // 방문한 것들: memberId와 themeId가 일치하는 VisitedPlace 엔티티가 존재하면서 MyPlace 필드가 true 경우
            visitedPlaces = visitedPlaceRepository.findByMyPlace_Member_MemberIdAndPlace_Theme_ThemeIdAndMyPlace_VisitedTrue(memberId, themeId);
        } else {
            // themeId가 제공되지 않은 경우, 모든 방문한 장소 반환
            visitedPlaces = visitedPlaceRepository.findByMyPlace_Member_MemberIdAndMyPlace_VisitedTrue(memberId);
        }

        return visitedPlaces.stream()
                .map(visitedPlace -> new VisitedPlaceListResponseDto(visitedPlace.getPlace()))
                .collect(Collectors.toList());
    }

    /***
     * VisitedPlace 엔티티가 존재하고 MyPlace의 visited가 true인 경우: visited를 false로 변경
     * VisitedPlace 엔티티가 존재하고 MyPlace의 visited가 false인 경우: visited를 true로 변경
     * VisitedPlace 엔티티가 존재하지 않는 경우: 새로운 VisitedPlace 엔티티를 생성하고 visited를 true로 설정
     *
     * @param memberId
     * @param placeId
     */
    @Transactional
    public void toggleVisited(Long memberId, Long placeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        // MyPlace 조회 또는 생성
        MyPlace myPlace = myPlaceRepository.findByMember_MemberId(memberId)
                .orElseGet(() -> MyPlace.builder()
                        .member(member)
                        .visited(false) // 초기값은 방문하지 않은 상태로 설정
                        .build());

        // VisitedPlace 조회
        Optional<VisitedPlace> visitedPlaceOpt = visitedPlaceRepository.findByMyPlace_MyPlaceIdAndPlace_PlaceId(myPlace.getMyPlaceId(), placeId);

        if (visitedPlaceOpt.isPresent()) {
            // 이미 방문한 장소라면, VisitedPlace를 삭제하고 MyPlace의 visited 상태를 false로 변경
            visitedPlaceRepository.delete(visitedPlaceOpt.get());
        } else {
            // 방문하지 않은 장소라면, 새로운 VisitedPlace 생성하고 MyPlace의 visited 상태를 true로 변경
            VisitedPlace visitedPlace = VisitedPlace.builder()
                    .myPlace(myPlace)
                    .place(place)
                    .build();
            visitedPlaceRepository.save(visitedPlace);
        }

        // MyPlace의 visited 상태를 토글
        myPlace.toggleVisited();

        // MyPlace 상태 저장
        myPlaceRepository.save(myPlace);
    }
}
