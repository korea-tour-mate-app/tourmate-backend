package com.snowflake_guide.tourmate.domain.visited_place.entity;

import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.domain.review.entity.Review;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "visited_place")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitedPlace extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitedPlaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private Boolean visited; // 가본 유무

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now(); // 생성일: 현재 날짜 할당되게 함

    @OneToMany(mappedBy = "visitedPlace")
    private List<Review> reviews;

    public void toggleVisited(){
        this.visited = !this.visited;
    }
}
