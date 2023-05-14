package com.idle.gaza.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "guide_thema")
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
public class GuideThema {

    @Column(name = "guide_thema_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int themaId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "guide_id")
    private Guide guide;//여러개의 테마가 한명의 가이드에게 사용될 수 있음

    @Column(name = "thema_code")
    private String themaCode;


    @Builder
    public GuideThema(int themaId, Guide guide, String themaCode) {
        this.themaId = themaId;
        this.guide = guide;
        this.themaCode = themaCode;
    }


}
