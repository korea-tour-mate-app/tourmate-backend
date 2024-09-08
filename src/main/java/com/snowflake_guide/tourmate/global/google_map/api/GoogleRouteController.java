package com.snowflake_guide.tourmate.global.google_map.api;

import com.snowflake_guide.tourmate.global.google_map.dto.GoogleRouteRequestDto;
import com.snowflake_guide.tourmate.global.google_map.dto.GoogleRouteResponseDto;
import com.snowflake_guide.tourmate.global.google_map.service.GoogleRouteService;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.RouteOptResponseDto;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.TMapRouteOptRequestDto;
import com.snowflake_guide.tourmate.global.tmap.routeopt.service.RouteOptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Google Directions API", description = "경유지 순서 최적화 해주는 Google Directions API 호출하여 경로 생성")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-map")
public class GoogleRouteController {

    private final GoogleRouteService googleRouteService;
    @PostMapping("/optimize-route/driving")
    public GoogleRouteResponseDto optimizeRoute(@RequestBody GoogleRouteRequestDto requestDto) {
        return googleRouteService.getTransitRouteSummary(requestDto);
    }
}
