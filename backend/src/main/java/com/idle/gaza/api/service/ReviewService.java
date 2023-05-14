package com.idle.gaza.api.service;

import com.idle.gaza.api.request.ReviewCreatePostRequest;
import com.idle.gaza.api.response.ReviewResponse;
import com.idle.gaza.db.entity.Review;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ReviewService {

    // 리뷰 작성
    Integer writeReview(ReviewCreatePostRequest reviewInfo);

    // 회원이 작성한 리뷰 리스트 조회
    List<ReviewResponse> getReviewsByUser(String userId);

    // 가이드에게 달린 리뷰 리스트 조회
    List<ReviewResponse> getReviewsByGuide(int guideId);

    List<ReviewResponse> getReviewsByGuideId(String guideId);
    Optional<Review> getReviewById(int reviewId);
}
