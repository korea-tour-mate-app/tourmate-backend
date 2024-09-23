package com.snowflake_guide.tourmate.domain.place.api;

import com.snowflake_guide.tourmate.domain.place.dto.GetPlaceByIdResponseDto;
import com.snowflake_guide.tourmate.domain.place.dto.GetPlacesByThemeResponseDto;
import com.snowflake_guide.tourmate.domain.place.service.PlaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Place API", description = "테마별 장소 리스트 및 상세정보 반환해주는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/themes")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping({"/{placeTheme}/places", "/places"})
    public ResponseEntity<GetPlacesByThemeResponseDto> getPlacesByTheme(
            @PathVariable(required = false) String placeTheme) {
        if (placeTheme == null) {
            placeTheme = "default"; // 기본값 설정
        }
        GetPlacesByThemeResponseDto response = placeService.getPlacesByTheme(placeTheme);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<GetPlaceByIdResponseDto> getPlaceById(@PathVariable Long placeId) {
        GetPlaceByIdResponseDto response = placeService.getPlaceById(placeId);
        return ResponseEntity.ok(response);
    }

}
