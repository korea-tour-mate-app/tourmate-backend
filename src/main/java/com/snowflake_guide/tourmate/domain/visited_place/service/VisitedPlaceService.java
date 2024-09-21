package com.snowflake_guide.tourmate.domain.visited_place.service;

import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.domain.place.repository.PlaceRepository;
import com.snowflake_guide.tourmate.domain.visited_place.dto.VisitedPlaceListResponseDto;
import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import com.snowflake_guide.tourmate.domain.visited_place.repository.VisitedPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitedPlaceService {
    private final VisitedPlaceRepository visitedPlaceRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<VisitedPlaceListResponseDto> getVisitedPlaces(String email, Long themeId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Long memberId = member.getMemberId();
        List<VisitedPlace> visitedPlaces;

        if (themeId != null) {
            // 특정 테마에 속하는 방문한 장소들만 조회
            visitedPlaces = visitedPlaceRepository.findByMember_MemberIdAndPlace_Theme_ThemeIdAndVisitedTrue(memberId, themeId);
        } else {
            // 모든 방문한 장소들 조회
            visitedPlaces = visitedPlaceRepository.findByMember_MemberIdAndVisitedTrue(memberId);
        }

        return visitedPlaces.stream()
                .map(visitedPlace -> new VisitedPlaceListResponseDto(visitedPlace.getPlace()))
                .collect(Collectors.toList());
    }

    /***
     * VisitedPlace 엔티티가 존재하고 visited가 true인 경우: visited를 false로 변경
     * VisitedPlace 엔티티가 존재하고 visited가 false인 경우: visited를 true로 변경
     * VisitedPlace 엔티티가 존재하지 않는 경우: 새로운 VisitedPlace 엔티티를 생성하고 visited를 true로 설정
     *
     */
    @Transactional
    public void toggleVisited(String email, Long placeId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        Long memberId = member.getMemberId();

        // VisitedPlace 조회
        Optional<VisitedPlace> visitedPlaceOpt = visitedPlaceRepository.findByMember_MemberIdAndPlace_PlaceId(memberId, placeId);

        if (visitedPlaceOpt.isPresent()) {
            // 이미 존재하는 VisitedPlace의 visited 상태를 토글
            VisitedPlace visitedPlace = visitedPlaceOpt.get();
            visitedPlace.toggleVisited(); // 상태 토글
            visitedPlaceRepository.save(visitedPlace); // 명시적으로 저장 (선택사항)
            log.info("회원 ID: {}와 장소 ID: {}에 대한 방문 상태가 변경되었습니다.", memberId, placeId);

        } else {
            // 새로운 VisitedPlace 생성
            VisitedPlace visitedPlace = VisitedPlace.builder()
                    .member(member)
                    .place(place)
                    .visited(true) // 초기값을 true로 설정
                    .createdAt(LocalDate.now()) // 명시적으로 설정
                    .build();
            visitedPlaceRepository.save(visitedPlace);
            log.info("회원 ID: {}와 장소 ID: {}에 대한 새로운 방문장소가 생성되었습니다.", memberId, placeId);
        }
    }
}