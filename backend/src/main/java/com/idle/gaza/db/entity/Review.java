package com.idle.gaza.db.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @Column(name="review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    //@ManyToOne
    @OneToOne
    @JoinColumn(name="reservation_id")
    private Reservation reservationId;

    private String content;
    @Column(name="created_date")
    private LocalDateTime createdDate;
    private int score;

    // == 생성 메서드 ==
    public static Review createReview(Reservation reservation, String content, int score){
        Review review = new Review();
        review.setReservationId(reservation);
        review.setContent(content);
        review.setScore(score);
        review.setCreatedDate(LocalDateTime.now());
        return review;
    }

}
