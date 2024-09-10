package com.snowflake_guide.tourmate.domain.member.repository;

import com.snowflake_guide.tourmate.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
        boolean existsByNickname(String username);
        boolean existsByEmail(String email);
        Optional<Member> findByEmail(String name);
}
