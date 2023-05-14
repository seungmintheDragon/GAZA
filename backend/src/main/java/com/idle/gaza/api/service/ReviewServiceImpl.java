package com.idle.gaza.api.service;

import com.idle.gaza.api.request.ReviewCreatePostRequest;
import com.idle.gaza.api.response.ReviewResponse;
import com.idle.gaza.db.entity.Reservation;
import com.idle.gaza.db.entity.Review;
import com.idle.gaza.db.repository.GuideRepository;
import com.idle.gaza.db.repository.ReservationRepository;
import com.idle.gaza.db.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service("reviewService")
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReservationRepository reservationRepository;

    @Override
    @Transactional
    public Integer writeReview(ReviewCreatePostRequest reviewInfo) {
        // 예약 조회
        int reservationId = reviewInfo.getReservationId();
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NoSuchElementException("Reservation not found"));

        // 완료된 예약인지 체크
        System.out.println(reservation.getStateCode());
        if(reservation.getStateCode().equals("RE02")){
            throw new IllegalStateException("대기 중인 상담에 대해서는 리뷰 작성이 불가능 합니다.");
        }

        // 리뷰 생성
        Review review = Review.createReview(reservation, reviewInfo.getContent(), reviewInfo.getScore());

        // 리뷰 저장
        reviewRepository.save(review);

        return review.getReviewId();
    }

    @Override
    public List<ReviewResponse> getReviewsByUser(String userId) {
        List<Review> reviews = reviewRepository.findReviewsByUser(userId);
        List<ReviewResponse> reviewRes = new ArrayList<>(reviews.size());
        for(int i=0; i<reviews.size(); i++){
            Review review = reviews.get(i);
            ReviewResponse res = new ReviewResponse(userId,
                    review.getReservationId().getGuideId().getGuideId(),
                    review.getReviewId(), review.getContent(), review.getCreatedDate(), review.getScore());
            reviewRes.add(res);
        }
        return reviewRes;
    }

    @Override
    public List<ReviewResponse> getReviewsByGuide(int guideId) {
        List<Review> reviews = reviewRepository.findReviewsByGuide(guideId);
        List<ReviewResponse> reviewRes = new ArrayList<>(reviews.size());
        for(int i=0; i<reviews.size(); i++){
            Review review = reviews.get(i);
            ReviewResponse res = new ReviewResponse(review.getReservationId().getUserId().getId(),
                    review.getReservationId().getGuideId().getGuideId(),
                    review.getReviewId(), review.getContent(), review.getCreatedDate(), review.getScore());
            reviewRes.add(res);
        }
        return reviewRes;
    }

    @Override
    public List<ReviewResponse> getReviewsByGuideId(String guideId) {
        List<Review> reviews = reviewRepository.findReviewsByGuideId(guideId);
        List<ReviewResponse> reviewRes = new ArrayList<>(reviews.size());
        for(int i=0; i<reviews.size(); i++){
            Review review = reviews.get(i);
            ReviewResponse res = new ReviewResponse(review.getReservationId().getUserId().getId(),
                    review.getReservationId().getGuideId().getGuideId(),
                    review.getReviewId(), review.getContent(), review.getCreatedDate(), review.getScore());
            reviewRes.add(res);
        }
        return reviewRes;
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId);
    }
}
