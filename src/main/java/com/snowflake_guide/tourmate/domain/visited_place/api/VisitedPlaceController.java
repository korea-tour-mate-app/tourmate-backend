package com.snowflake_guide.tourmate.domain.visited_place.api;

import com.snowflake_guide.tourmate.domain.like.dto.LikeListResponseDto;
import com.snowflake_guide.tourmate.domain.like.service.LikeService;
import com.snowflake_guide.tourmate.domain.visited_place.dto.VisitedPlaceListResponseDto;
import com.snowflake_guide.tourmate.domain.visited_place.service.VisitedPlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Visited API", description = "사용자가 방문한 장소를 조회하고 토글하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visited")
public class VisitedPlaceController {
    private final VisitedPlaceService visitedPlaceService;

    @Operation(summary = "방문한 장소 리스트 조회", description = "특정 사용자가 방문한 모든 장소를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<VisitedPlaceListResponseDto>> getVisitedPlaces(Authentication authentication,
                                                                              @RequestParam(required = false) Long themeId) {
        String email = authentication.getName();
        List<VisitedPlaceListResponseDto> visitedPlaces = visitedPlaceService.getVisitedPlaces(email, themeId);
        return ResponseEntity.ok(visitedPlaces);
    }

    @Operation(summary = "방문 상태 토글", description = "특정 장소에 대한 방문 상태를 토글합니다.")
    @PostMapping("/{placeId}")
    public ResponseEntity<String> toggleVisited(Authentication authentication, @PathVariable("placeId") Long placeId) {

        String email = authentication.getName();
        visitedPlaceService.toggleVisited(email, placeId);
        return ResponseEntity.ok("방문 상태가 성공적으로 토글되었습니다.");
    }
}
