package com.snowflake_guide.tourmate.domain.baggage_storage.service;

import com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto;
import com.snowflake_guide.tourmate.domain.baggage_storage.entity.BaggageStorage;
import com.snowflake_guide.tourmate.domain.baggage_storage.repository.BaggageStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetBaggageService {
    private final BaggageStorageRepository baggageStorageRepository;

    // 모든 수하물을 반환하되 parentName을 기준으로 첫 번째 요소만 반환
    public List<BaggageStorageResponseDto> getAllBaggageStorageDetails() {
        return baggageStorageRepository.findAllGroupedByParentName();
    }
}

