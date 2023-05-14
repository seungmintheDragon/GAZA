package com.idle.gaza.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ApiModel("DayOffPostRequest")
public class DayOffPostRequest {

    String userId;

    @JsonSerialize(as = LocalDate.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate day;

}
