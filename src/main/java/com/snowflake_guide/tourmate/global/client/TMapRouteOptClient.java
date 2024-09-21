package com.snowflake_guide.tourmate.global.client;


import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.TMapRouteOptRequestDto;
import com.snowflake_guide.tourmate.global.tmap.routeopt.dto.TMapRouteOptResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "TMapClient", url = "https://apis.openapi.sk.com/tmap")
public interface TMapRouteOptClient {
    @PostMapping("/routes/routeOptimization10")
    TMapRouteOptResponseDto optimizeRoute(
            @RequestParam("version") String version,
            @RequestHeader("appKey") String appKey,
            @RequestBody TMapRouteOptRequestDto requestDto
    );
}
