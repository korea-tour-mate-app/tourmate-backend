package com.snowflake_guide.tourmate.domain.baggage_storage.repository;

import com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto;
import com.snowflake_guide.tourmate.domain.baggage_storage.entity.BaggageStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggageStorageRepository extends JpaRepository<BaggageStorage, Long> {
    @Query("SELECT new com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto" +
            "(b.baggageStorageId, b.lineNumber, b.lockerName, b.latitude, b.longitude, b.lockerDetail) " +
            "FROM BaggageStorage b")
    List<BaggageStorageResponseDto> findAllBaggageStorageDetails();
}