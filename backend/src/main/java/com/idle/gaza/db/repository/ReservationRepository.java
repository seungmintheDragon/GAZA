package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 예약 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query(value="select * from reservation where guide_id = (select guide_id from guide where user_id = (select user_id from user where id= :guideId))", nativeQuery = true)
    List<Reservation> findResevationsByGuide(@Param("guideId") String guideId);
    @Query(value="select * from reservation where user_id = (select user_id from user where id= :userId)", nativeQuery = true)
    List<Reservation> findResevationsByUser(@Param("userId") String userId);
    @Query(value="SELECT consulting_date FROM reservation WHERE guide_id = :guideId AND DATE(consulting_date) = DATE(:selectedDate); ", nativeQuery = true)
    List<Timestamp> getImpossibleTime(@Param("guideId") int guideId, @Param("selectedDate") Date selectedDate);


    /* 해당 가이드의 예약 정보를 가져옴 */
    @Query(value = "SELECT * FROM reservation WHERE guide_id=:guideId", nativeQuery = true)
    List<Reservation> searchReservationByGuideId(@Param("guideId") int guideId);
}
