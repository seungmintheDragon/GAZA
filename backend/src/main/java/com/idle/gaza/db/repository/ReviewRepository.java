package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "select * from review where reservation_id in (select reservation_id from reservation where user_id = (select user_id from user where id= :userId))", nativeQuery = true)
    List<Review> findReviewsByUser(@Param("userId") String userId);

    @Query(value = "select * from review where reservation_id in (select reservation_id from reservation where guide_id = :guideId)", nativeQuery = true)
    List<Review> findReviewsByGuide(@Param("guideId") int guideId);

    @Query(value = "select * from review where reservation_id in (select reservation_id from reservation where guide_id = (select guide_id from guide where user_id = (select user_id from user where id= :guideId)))", nativeQuery = true)
    List<Review> findReviewsByGuideId(@Param("guideId") String guideId);

    
}
