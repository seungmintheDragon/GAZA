package com.idle.gaza.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@ApiModel("GuideRequest")
public class GuideRequest {

    private int guideId;//guide pk

    private String id;//user pk

    private String onlineIntroduction;

    private String introduction;

    private String picture;

    private String country;

    private String city;

//    @JsonSerialize(as = LocalTime.class)
//    @JsonFormat(pattern = "kk:mm:ss")
//    private LocalTime closeTimeStart;
//
//    @JsonSerialize(as = LocalTime.class)
//    @JsonFormat(pattern = "kk:mm:ss")
//    private LocalTime closeTimeEnd;

    private String timeStart;
    private String timeEnd;

    private Integer price;

    private Integer license;


}
