package com.snowflake_guide.tourmate.domain.like.api;

import com.snowflake_guide.tourmate.domain.like.dto.LikeListResponseDto;
import com.snowflake_guide.tourmate.domain.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Review API", description = "테마별 리뷰를 조회, 등록, 삭제하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요한 장소 리스트 조회", description = "특정 사용자가 좋아요한 모든 장소를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LikeListResponseDto>> getLikedPlaces(@RequestParam Long memberId,
                                                                    @RequestParam(required = false) Long themeId) {
        // TODO 로그인 멤버 추출로 변경
        List<LikeListResponseDto> likedPlaces = likeService.getLikedPlaces(memberId, themeId);
        return ResponseEntity.ok(likedPlaces);
    }
}
