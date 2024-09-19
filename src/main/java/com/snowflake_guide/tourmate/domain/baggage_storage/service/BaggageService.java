package com.snowflake_guide.tourmate.domain.baggage_storage.service;

import com.snowflake_guide.tourmate.domain.baggage_storage.entity.BaggageStorage;
import com.snowflake_guide.tourmate.domain.baggage_storage.repository.BaggageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaggageService {
    private final BaggageRepository baggageRepository; // 'final' 키워드 추가

    public void saveBaggageFromCSV(String filePath) throws IOException, CsvValidationException {
        List<BaggageStorage> baggages = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                // line[4]를 ,로 분리하여 위도와 경도 값 추출
                String[] location = line[4].split(",");
                double latitude = parseDoubleOrDefault(location[0].trim(), 0.0);
                double longitude = parseDoubleOrDefault(location[1].trim(), 0.0);

                // 숫자 필드 값 처리 (빈 문자열일 경우 기본값 사용)
                int lineNumber = parseIntOrDefault(line[1], 0);
                int smallCount = parseIntOrDefault(line[5], 0);
                int mediumCount = parseIntOrDefault(line[6], 0);
                int largeCount = parseIntOrDefault(line[7], 0);
                int controllerCount = parseIntOrDefault(line[8], 0);
                int columnCount = parseIntOrDefault(line[9], 0);

                // BaggageStorage 객체를 Builder 패턴으로 생성
                BaggageStorage baggage = BaggageStorage.builder()
                        .lineNumber(lineNumber)
                        .lockerName(line[2])
                        .lockerDetail(line[3])
                        .latitude(latitude)
                        .longitude(longitude)
                        .smallCount(smallCount)
                        .mediumCount(mediumCount)
                        .largeCount(largeCount)
                        .controllerCount(controllerCount)
                        .columnCount(columnCount)
                        .build();

                baggages.add(baggage);
            }
        }
        baggageRepository.saveAll(baggages);
    }
    // 문자열을 파싱하여 Integer로 변환, 빈 문자열일 경우 기본값 반환
    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // 문자열을 파싱하여 Double로 변환, 빈 문자열일 경우 기본값 반환
    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
