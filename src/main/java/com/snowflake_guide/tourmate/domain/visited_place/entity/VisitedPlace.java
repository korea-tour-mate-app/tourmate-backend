package com.snowflake_guide.tourmate.domain.visited_place.entity;

import com.snowflake_guide.tourmate.domain.my_place.entity.MyPlace;
import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.domain.review.entity.Review;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import lombok.*;
import jakarta.persistence.*;

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
    @JoinColumn(name = "my_place_id", nullable = false)
    private MyPlace myPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "visitedPlace")
    private List<Review> reviews;
}
