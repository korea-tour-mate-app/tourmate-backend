package com.snowflake_guide.tourmate.domain.baggage_storage.repository;

import com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageDetailResponseDto;
import com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto;
import com.snowflake_guide.tourmate.domain.baggage_storage.entity.BaggageStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggageStorageRepository extends JpaRepository<BaggageStorage, Long> {
    @Query("SELECT new com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageResponseDto(" +
            "b.parentName, b.baggageStorageId, b.lineNumber, b.longitude, b.latitude) " +
            "FROM BaggageStorage b " +
            "WHERE b.baggageStorageId = (SELECT MIN(bs.baggageStorageId) FROM BaggageStorage bs WHERE bs.parentName = b.parentName)")
    List<BaggageStorageResponseDto> findAllGroupedByParentName();

    // SQL 쿼리를 사용하여 parentName(역 이름)으로 필터링된 수하물 정보를 조회
    @Query("SELECT new com.snowflake_guide.tourmate.domain.baggage_storage.dto.BaggageStorageDetailResponseDto(" +
            "b.lockerName, b.lockerDetail, b.smallCount, b.mediumCount, b.largeCount, b.controllerCount, b.columnCount) " +
            "FROM BaggageStorage b " +
            "WHERE b.parentName = :stationName")
    List<BaggageStorageDetailResponseDto> findByParentName(@Param("stationName") String stationName);
}