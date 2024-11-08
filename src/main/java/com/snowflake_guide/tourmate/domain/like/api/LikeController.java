package com.snowflake_guide.tourmate.domain.like.api;

import com.snowflake_guide.tourmate.domain.like.dto.LikeListResponseDto;
import com.snowflake_guide.tourmate.domain.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Like API", description = "사용자가 좋아요한 장소를 조회하고 토글하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요한 장소 리스트 조회", description = "특정 사용자가 좋아요한 모든 장소를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LikeListResponseDto>> getLikedPlaces(Authentication authentication,
                                                                    @RequestParam(required = false) Long themeId) {
        // JWT에서 추출한 이메일로 Member 조회
        String email = authentication.getName();
        List<LikeListResponseDto> likedPlaces = likeService.getLikedPlaces(email, themeId);
        return ResponseEntity.ok(likedPlaces);
    }

    @Operation(summary = "좋아요 토글", description = "특정 장소에 대한 좋아요 상태를 토글합니다.")
    @PostMapping("/{placeId}")
    public ResponseEntity<String> toggleLike(Authentication authentication, @PathVariable("placeId") Long placeId) {

        String email = authentication.getName();
        likeService.toggleLike(email, placeId);
        return ResponseEntity.ok("좋아요가 성공적으로 토글되었습니다.");
    }
}
