package com.snowflake_guide.tourmate.domain.place.repository;

import com.snowflake_guide.tourmate.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 테마 ID로 장소 리스트를 가져오는 쿼리
    @Query("SELECT p FROM Place p WHERE p.theme.placeTheme = :placeTheme")
    List<Place> findPlacesByPlaceTheme(@Param("placeTheme") String placeTheme);
}
