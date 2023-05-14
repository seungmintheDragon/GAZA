package com.idle.gaza.api.request;

import com.idle.gaza.db.entity.Guide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("ReservationCreatePostRequest")
public class ReservationCreatePostRequest {
    private String userId;
    private Integer guideId;
    @ApiModelProperty(name="상담하는 날짜시간", example="2023-02-01T08:11:11")
    private LocalDateTime consultingDate;
    @ApiModelProperty(name="여행 시작 날짜시간", example="2023-02-01T08:11:11")
    private LocalDateTime travelStartDate;
    @ApiModelProperty(name="여행 끝  날짜시간", example="2023-02-01T08:11:11")
    private LocalDateTime travelEndDate;
    private int numberOfPeople;
    private int withChildren;
    private int withElderly;
    private int withDisabled;
    private String note;
}
