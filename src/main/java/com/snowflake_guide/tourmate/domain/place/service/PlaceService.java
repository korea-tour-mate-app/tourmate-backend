package com.snowflake_guide.tourmate.domain.place.service;

import com.snowflake_guide.tourmate.domain.place.dto.GetPlaceByIdResponseDto;
import com.snowflake_guide.tourmate.domain.place.dto.GetPlacesByThemeResponseDto;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public GetPlacesByThemeResponseDto getPlacesByTheme(String placeTheme) {
        // PlaceRepository를 이용해 테마 이름으로 장소 리스트를 가져옴
        List<Place> places = placeRepository.findPlacesByPlaceTheme(placeTheme);

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
                .orElseThrow(() -> new IllegalArgumentException("해당 장소가 존재하지 않습니다: " + placeId));
        return GetPlaceByIdResponseDto.fromEntity(place);
    }
}
