package com.snowflake_guide.tourmate.domain.baggage_storage.repository;

import com.snowflake_guide.tourmate.domain.baggage_storage.entity.BaggageStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaggageRepository extends JpaRepository<BaggageStorage, Long> {
}