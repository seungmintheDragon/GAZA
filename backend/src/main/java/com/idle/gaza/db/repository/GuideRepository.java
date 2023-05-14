package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.Guide;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/*
* 가이드 관련 JPA Query Method 인터페이스 정의
* */
@Repository
public interface GuideRepository extends JpaRepository<Guide, Integer> {

    /* 전체 가이드 회원 조회 */
    List<Guide> findBy();

    /* 가이드 상세 조회 */
    Optional<Guide> findGuideByGuideId(int guideId);

    /* 해당 회원이 가이드인지 조회 */
    @Query(value = "SELECT *  FROM guide as g WHERE g.user_id=:userId", nativeQuery = true)
    Optional<Guide> findGuideByUser(@Param("userId") int userId);

    /* 예약순 조회 */
    @Query(value = "select * from guide left join ( select guide_id, count(guide_id) from reservation group by guide_id order by count(*)) reservation on reservation.guide_id=guide.guide_id order by reservation.guide_id", nativeQuery = true)
    List<Guide> findOrderByMaxReservation();

    Guide findByUserId_Id(String guiedId);

    /* 도시 또는 나라로 가이드를 조회함 */
    @Query(value = "SELECT * FROM guide as g WHERE g.city=:searchName OR g.country=:searchName", nativeQuery = true)
    List<Guide> searchByCountryOrCity(@Param("searchName") String searchName);


    /* 테마 코드에 해당하는 테마 이름을 가져옴 */
    @Query(value = "SELECT c.description FROM code as c WHERE c.name=:searchCode", nativeQuery = true)
    String searchNameByCode(@Param("searchCode") String searchCode);

    @Query(value="select * from guide left join guide_thema on guide.guide_id = guide_thema.guide_id where thema_code = (select thema_code from code where description=:themaName)",nativeQuery = true)
    List<Guide> searchGuideByThema(@Param("themaName") String themaName);

    @Query(value = "select * from guide where guide_id IN (select guide_id from guide_thema where thema_code=:code)", nativeQuery = true)
    List<Guide> searchByCode(@Param("code") String code);


    @Modifying
    @Transactional
    @Query(value = "update guide set close_time_start=0 , close_time_end=0 where guide_id=:guideId", nativeQuery = true)
    int updateTime(@Param("guideId") int guideId);
}
