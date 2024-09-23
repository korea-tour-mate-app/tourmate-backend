package com.snowflake_guide.tourmate.domain.place.service;

import com.snowflake_guide.tourmate.domain.place.dto.GetPlaceByIdResponseDto;
import com.snowflake_guide.tourmate.domain.place.dto.GetPlacesByThemeResponseDto;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public GetPlacesByThemeResponseDto getPlacesByTheme(String placeTheme) {

        List<Place> places;

        if (Objects.equals(placeTheme, "default") || placeTheme == null || placeTheme.trim().isEmpty()) {
            // placeTheme이 비어있으면 모든 장소를 조회
            places = placeRepository.findAll();
            log.info("모든 장소를 반환합니다.");
        } else {
            // placeTheme으로 장소 리스트 조회
            places = placeRepository.findPlacesByPlaceTheme(placeTheme);

            // 장소 리스트가 비어있을 경우 예외 발생
            if (places.isEmpty()) {
                log.warn("해당 테마에 대한 장소가 존재하지 않습니다: {}", placeTheme);
                throw new NoSuchElementException("해당 테마에 대한 장소가 존재하지 않습니다.");
            }
        }
        // 장소 리스트를 DTO 리스트로 변환
        List<GetPlacesByThemeResponseDto.PlaceDto> placeDtos = places.stream()
                .map(place -> new GetPlacesByThemeResponseDto.PlaceDto(
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getPlaceName(),
                        place.getPlaceId()
                ))
                .collect(Collectors.toList());

        // 테마 ID와 테마 이름을 포함한 Response DTO 생성
        Long themeId = places.isEmpty() ? null : places.get(0).getTheme().getThemeId();
        return new GetPlacesByThemeResponseDto(themeId, placeTheme, placeDtos);
    }

    public GetPlaceByIdResponseDto getPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> {
                    log.warn("해당 장소가 존재하지 않습니다: {}", placeId);
                    return new NoSuchElementException("해당 장소가 존재하지 않습니다: " + placeId);
                });
        return GetPlaceByIdResponseDto.fromEntity(place);
    }
}
