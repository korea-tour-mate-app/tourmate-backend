package com.snowflake_guide.tourmate.domain.baggage_storage.service;

import com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto;
import com.snowflake_guide.tourmate.domain.baggage_storage.repository.BaggageStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetBaggageService {
    private final BaggageStorageRepository baggageStorageRepository; // 'final' 키워드 추가

    public List<BaggageStorageResponseDto> getAllBaggageStorageDetails() {
        return baggageStorageRepository.findAllBaggageStorageDetails();
    }
}
