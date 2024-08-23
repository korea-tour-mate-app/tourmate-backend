package com.snowflake_guide.tourmate.domain.my_place.repository;

import com.snowflake_guide.tourmate.domain.my_place.entity.MyPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPlaceRepository extends JpaRepository<MyPlace, Long> {
}
