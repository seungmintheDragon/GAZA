package com.idle.gaza.db.entity;

import io.jsonwebtoken.lang.Assert;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Reservation {
    @Id
    @Column(name="reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name="guide_id")
    private Guide guideId;

    @Column(name="state_code")
    private String stateCode;

    @Column(name="consulting_date")
    private LocalDateTime consultingDate;

    @Column(name="reservation_date")
    private LocalDateTime reservationDate;

    @Column(name="travel_start_date")
    private LocalDateTime travelStartDate;

    @Column(name="travel_end_date")
    private LocalDateTime travelEndDate;

    @Column(name="number_of_people")
    private Integer numberOfPeople;

    @Column(name="with_children", columnDefinition = "TINYINT", length=1)
    private Integer withChildren;

    @Column(name="with_elderly", columnDefinition = "TINYINT", length=1)
    private Integer withElderly;

    @Column(name="with_disabled", columnDefinition = "TINYINT", length=1)
    private Integer withDisabled;

    private String note;

    @OneToOne(mappedBy = "reservationId")
    @JoinColumn(name = "review_id")
    private Review review;

    @JoinColumn(name = "session_id")
    private String sessionId;

    @Builder
    public Reservation(User userId, Guide guideId, LocalDateTime consultingDate, LocalDateTime reservationDate, LocalDateTime travelStartDate, LocalDateTime travelEndDate, int numberOfPeople, int withChildren, int withElderly, int withDisabled, String note, String stateCode) {
        this.userId = userId;
        this.guideId = guideId;
        this.consultingDate = consultingDate;
        this.reservationDate = reservationDate;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.numberOfPeople = numberOfPeople;
        this.withChildren = withChildren;
        this.withElderly = withElderly;
        this.withDisabled = withDisabled;
        this.note = note;
        this.stateCode = stateCode;
    }
}
