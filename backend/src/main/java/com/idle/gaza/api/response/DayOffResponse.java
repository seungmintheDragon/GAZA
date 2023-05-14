package com.idle.gaza.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@ApiModel("DayOffResponse")
public class DayOffResponse {

    int dayOffId;
    int guideId;
    @JsonSerialize(as = LocalDate.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate day;

    @Builder
    public DayOffResponse(int dayOffId, int guideId, LocalDate day) {
        this.dayOffId = dayOffId;
        this.guideId = guideId;
        this.day = day;
    }
}
