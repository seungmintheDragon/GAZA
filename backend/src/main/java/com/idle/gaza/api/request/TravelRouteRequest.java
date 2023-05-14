package com.idle.gaza.api.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ApiModel("TravleRouteRequest")
public class TravelRouteRequest {
    String name;
    String address;
    String latitude;
    String longitude;

    public TravelRouteRequest() {
    }
}
