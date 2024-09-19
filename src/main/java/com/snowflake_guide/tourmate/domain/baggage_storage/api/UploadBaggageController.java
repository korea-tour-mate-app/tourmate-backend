package com.snowflake_guide.tourmate.domain.baggage_storage.api;

import com.opencsv.exceptions.CsvValidationException;
import com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto;
import com.snowflake_guide.tourmate.domain.baggage_storage.service.GetBaggageService;
import com.snowflake_guide.tourmate.domain.baggage_storage.service.UploadBaggageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Tag(name = "Baggage Storage API", description = "짐 보관소 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/baggage")

public class UploadBaggageController {

    private final GetBaggageService getBaggageService;

    @GetMapping("/all-details")
    public List<BaggageStorageResponseDto> getAllBaggageStorageDetails() {
        return getBaggageService.getAllBaggageStorageDetails();
    }
}