package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.Guide;
import com.idle.gaza.db.entity.GuideThema;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface GuideThemaRepository extends JpaRepository<GuideThema, Integer> {

    List<GuideThema> findByGuide(Guide guide);


    @Modifying
    @Transactional
    @Query(value = "delete from guide_thema where guide_id=:guideId and guide_thema_id=:themaId", nativeQuery = true)
    int delThema(@Param("guideId") int guideId, @Param("themaId") int themaId);


}
