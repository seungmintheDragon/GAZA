package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.DayOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DayOffRepository extends JpaRepository<DayOff, Integer> {

    Optional<DayOff> findDayOffByDayOffId(int dayId);

    @Query(value = "select * from dayoff as d where d.guide_id=:guideId", nativeQuery = true)
    List<DayOff> searchDayOffByGuide(@Param("guideId") int guideId);

}
