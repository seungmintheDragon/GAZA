package com.idle.gaza.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("LocationResponse")
public class LocationResponse {

    private int locationId;//pk

    private String name;
    private String address;
    private String categoryCode;
    private String latitude;
    private String longitude;
    private String picture;

    @Builder
    public LocationResponse(int locationId, String name, String address, String categoryCode, String latitude, String longitude, String picture) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.categoryCode = categoryCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.picture = picture;
    }
}
