package com.idle.gaza.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ApiModel("LocationPostRequest")
@ToString
public class LocationPostRequest {

    private Integer recommendId;

    //private Integer guideId;

    private String name;

    private String address;

    //private String categoryCode;

    private String picture;

//    private String latitude;
//
//    private String longitude;

    private String categoryName;

    private String loginId;

}
