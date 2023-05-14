package com.idle.gaza.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("ReservationResponse")
public class ReservationResponse {
    @ApiModelProperty(name="가이드 사진")
    String picture;
    int guidePk;
    int reservationId;
    String guideName;
    String guideId;
    int numberOfPeople;
    int withChildren;
    int withElderly;
    int withDisabled;
    String note;
    LocalDateTime consultingDate;
    LocalDateTime travelStartDate;
    LocalDateTime travelEndDate;
    String stateCode;

    String sessionId;

    String userName;

    // 예약 내역 제목 정하면 그거 정보도 넘겨주고.
    // 예약 상태에 따라 화상상담 정보랑 일정 정보도 넘겨야줘야햄.


    @Builder
    public ReservationResponse(int guidePk, String userName, String picture, int reservationId, String guideName, String guideId, int numberOfPeople, int withChildren, int withElderly, int withDisabled, String note, LocalDateTime consultingDate, LocalDateTime travelStartDate, LocalDateTime travelEndDate, String stateCode, String sessionId) {
        this.guidePk = guidePk;
        this.userName = userName;
        this.picture = picture;
        this.reservationId = reservationId;
        this.guideName = guideName;
        this.guideId = guideId;
        this.numberOfPeople = numberOfPeople;
        this.withChildren = withChildren;
        this.withElderly = withElderly;
        this.withDisabled = withDisabled;
        this.note = note;
        this.consultingDate = consultingDate;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.stateCode = stateCode;
        this.sessionId = sessionId;
    }
}
