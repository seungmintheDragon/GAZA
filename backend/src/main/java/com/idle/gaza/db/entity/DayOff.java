package com.idle.gaza.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dayoff")
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
public class DayOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dayoff_id")
    private Integer dayOffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="guide_id")
    private Guide guide;

    @Column(name="dayoff_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dayOffDate;

    @Builder
    public DayOff(Integer dayOffId, Guide guide, LocalDate dayOffDate) {
        this.dayOffId = dayOffId;
        this.guide = guide;
        this.dayOffDate = dayOffDate;
    }

    public DayOff() {

    }


}
