package com.snowflake_guide.tourmate.domain.my_place.entity;

import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "my_place")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPlace extends BaseTimeEntity { // 내 장소 리스트

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myPlaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Boolean visited; // 가본 유무

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now(); // 생성일: 현재 날짜 할당되게 함

    public void toggleVisited(){
        this.visited = !this.visited;
    }
}
