package com.snowflake_guide.tourmate.domain.baggage_storage.api;

import com.opencsv.exceptions.CsvValidationException;
import com.snowflake_guide.tourmate.domain.baggage_storage.service.BaggageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Baggage Storage API", description = "짐 보관소 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/baggage")

public class BaggageController {

    private final BaggageService baggageService;

    @GetMapping("/upload-baggage") // csv 파일을 baggage 엔티티에 넣는 방법
    public String uploadBaggageData(@RequestParam String filePath) { // 실제 csv 파일 경로 예시: C:\\Users\\21keu\\OneDrive\\문서\\졸작\\baggage_final.csv
        try {
            baggageService.saveBaggageFromCSV(filePath);
            return "CSV 데이터를 성공적으로 저장했습니다!";
        } catch (IOException | CsvValidationException e) {
            return "CSV 데이터 저장 중 오류 발생: " + e.getMessage();
        }
    }
}