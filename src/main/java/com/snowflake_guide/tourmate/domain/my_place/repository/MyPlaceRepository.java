package com.snowflake_guide.tourmate.domain.my_place.repository;

import com.snowflake_guide.tourmate.domain.my_place.entity.MyPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyPlaceRepository extends JpaRepository<MyPlace, Long> {
    // Member ID로 MyPlace를 조회
    Optional<MyPlace> findByMember_MemberId(Long memberId);
}
