package com.snowflake_guide.tourmate.domain.like.api;

import com.snowflake_guide.tourmate.domain.like.service.LikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review API", description = "테마별 리뷰를 조회, 등록, 삭제하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
    private final LikeService likeService;
}
