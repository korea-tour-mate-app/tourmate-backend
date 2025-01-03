package com.snowflake_guide.tourmate.domain.member.entity;

import com.snowflake_guide.tourmate.domain.like.entity.Like;
import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 100, nullable = false, unique = true)
    private String nickname; // 닉네임은 중복 불가능

    private String password;

    @Column(nullable = false, unique = true)
    private String email; // 이메일은 중복 불가능

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDate createdAt = LocalDate.now(); // 현재 날짜 할당되게 함

    @OneToMany(mappedBy = "member")
    private List<Like> likes;

    @OneToMany(mappedBy = "member")
    private List<VisitedPlace> visitedPlaces;
}