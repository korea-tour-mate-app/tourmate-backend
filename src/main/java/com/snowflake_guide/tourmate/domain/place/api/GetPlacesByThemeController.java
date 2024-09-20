package com.snowflake_guide.tourmate.domain.place.api;

import com.snowflake_guide.tourmate.domain.place.dto.ThemePlacesResponseDto;
import com.snowflake_guide.tourmate.domain.place.service.GetPlacesByThemeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Theme Place API", description = "테마별 장소 리스트 반환해주는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/themes")
public class GetPlacesByThemeController {
    private final GetPlacesByThemeService getPlacesByThemeService;

    @GetMapping("/{placeTheme}/places")
    public ResponseEntity<ThemePlacesResponseDto> getPlacesByTheme(@PathVariable String placeTheme) {
        ThemePlacesResponseDto response = getPlacesByThemeService.getPlacesByTheme(placeTheme);
        return ResponseEntity.ok(response);
    }
}
