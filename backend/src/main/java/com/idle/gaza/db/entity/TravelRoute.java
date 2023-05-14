package com.idle.gaza.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelRoute {
    @Id
    @Column(name="travel_route_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer TravelRouteId;

    //@ManyToOne
    @OneToOne
    @JoinColumn(name="reservation_id")
    private Reservation reservationId;
    private String name;
    private String address;

    @Column(name = "order_no")
    private int orderNo;
    @Column(columnDefinition = "DECIMAL")
    private String latitude;
    @Column(columnDefinition = "DECIMAL")
    private String longitude;

    public TravelRoute(Reservation reservationId, String name, String address, int orderNo, String latitude, String longitude) {
        this.reservationId = reservationId;
        this.name = name;
        this.address = address;
        this.orderNo = orderNo;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
