package com.idle.gaza.db.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


/**
 * 가이드 추천 장소 엔티티
 */
@Entity(name = "recommend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@DynamicInsert
public class GuideRecommendLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recommend_id")
    private Integer recommendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="guide_id")
    private Guide guide;

    private String name;

    private String address;

    @Column(name="category_code")
    private String categoryCode;

    private String picture;

    @Column(columnDefinition = "DECIMAL")
    private String latitude;

    @Column(columnDefinition = "DECIMAL")
    private String longitude;
}
