package com.snowflake_guide.tourmate.domain.baggage_storage.api;

import com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto;
import com.snowflake_guide.tourmate.domain.baggage_storage.service.GetBaggageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Baggage Storage API", description = "짐 보관소 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/baggage")

public class GetBaggageController {

    private final GetBaggageService getBaggageService;

    // 모든 수하물을 그룹화하여 반환하는 API
    @GetMapping("/all-grouped")
    public List<BaggageStorageResponseDto> getAllGroupedBaggageStorageDetails() {
        return getBaggageService.getAllBaggageStorageDetails();
    }
}