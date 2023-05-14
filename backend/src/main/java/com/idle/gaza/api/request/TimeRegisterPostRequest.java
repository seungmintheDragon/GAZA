package com.idle.gaza.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/*
* 가이드 상담 시간대 설정을 받는 클래스
* */
@Getter
@Setter
@ApiModel("TimeRegisterPostRequest")
public class TimeRegisterPostRequest {

    private String userId;

//    @JsonSerialize(as = LocalTime.class)
//    @JsonFormat(pattern = "kk:mm:ss")
//    private LocalTime startTime;
//
//    @JsonSerialize(as = LocalTime.class)
//    @JsonFormat(pattern = "kk:mm:ss")
//    private LocalTime endTime;

    private String timeStart;
    private String timeEnd;

}
