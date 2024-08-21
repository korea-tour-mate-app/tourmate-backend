package com.snowflake_guide.tourmate.domain.member.entity;

import com.snowflake_guide.tourmate.domain.like.entity.Like;
import com.snowflake_guide.tourmate.domain.my_place.entity.MyPlace;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String name;

    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now(); // 현재 날짜 할당되게 함

    @OneToMany(mappedBy = "member")
    private List<Like> likes;

    @OneToMany(mappedBy = "member")
    private List<MyPlace> myPlaces;
}