package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.TravelRoute;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRouteRepository extends JpaRepository<TravelRoute, Integer> {
    @Query(value="select * from travel_route where reservation_id = :reservationId", nativeQuery = true)
    List<TravelRoute> findByRerservationId_ReservationId(Integer reservationId);

    @Query(value = "select * from travel_route where reservation_id = :reservationId order by order_no asc", nativeQuery = true)
    List<TravelRoute> getRouteOrderList(@Param("reservationId") int reservationId);

}
