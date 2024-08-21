package com.snowflake_guide.tourmate.domain.theme.entity;

import com.snowflake_guide.tourmate.domain.place.entity.Place;
import com.snowflake_guide.tourmate.global.util.BaseTimeEntity;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "theme")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theme extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeId;

    @Column(length = 100, nullable = false)
    private String placeTheme; // 테마 이름

    @OneToMany(mappedBy = "theme")
    private List<Place> places;
}

