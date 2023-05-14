package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.GuideRecommendLocation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 가이드 추천 장소 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의
 */
@Repository
public interface GuideRecommendRepository extends JpaRepository<GuideRecommendLocation, Integer>{


    /* 해당 가이드의 추천 장소를 조회 */
    //Optional<GuideRecommendLocation> findBy();

    Optional<GuideRecommendLocation> findByRecommendId(int recommendId);

    @Modifying
    @Transactional
    @Query(value = "delete from recommend where recommend_id=:recommendId and guide_id=:guideId", nativeQuery = true)
    int delRecommendLocation(@Param("recommendId") int recommendId, @Param("guideId") int guideId);

    List<GuideRecommendLocation> findByGuide_UserId_Id(String guideId);
}
