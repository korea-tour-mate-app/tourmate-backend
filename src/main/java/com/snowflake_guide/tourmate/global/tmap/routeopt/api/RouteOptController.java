package com.snowflake_guide.tourmate.global.tmap.routeopt.api;

import com.snowflake_guide.tourmate.global.tmap.routeopt.service.RouteOptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TMap API", description = "경유지 순서 최적화 해주는 TMap API 호출하여 경로 생성")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tmap")
public class RouteOptController {
    private final RouteOptService routeOptService;

    // @PostMapping("/optimize-route")
}
