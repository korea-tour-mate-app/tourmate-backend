package com.snowflake_guide.tourmate.domain.visited_place.repository;

import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitedPlaceRepository extends JpaRepository<VisitedPlace, Long> {
    List<VisitedPlace> findByPlace_PlaceId(Long placeId);
}
