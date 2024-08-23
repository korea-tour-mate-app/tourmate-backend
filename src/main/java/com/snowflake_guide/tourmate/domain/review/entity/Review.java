package com.snowflake_guide.tourmate.domain.review.entity;
import com.snowflake_guide.tourmate.domain.visited_place.entity.VisitedPlace;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visited_place_id", nullable = false)
    private VisitedPlace visitedPlace;

    @Lob // 글자수 제한 x
    @Column(nullable = false)
    private String reviewDec; // 리뷰 내용

    @Column(nullable = false)
    private Float rate; // 평점 (1~5, 0.5간격)
    private String reviewUrl1;
    private String reviewUrl2;
    private String reviewUrl3;
}
