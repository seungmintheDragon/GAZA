package com.idle.gaza.api.response;

import com.idle.gaza.db.entity.Reservation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("ReviewResponse")
@AllArgsConstructor
public class ReviewResponse {
    @ApiModelProperty(name="User ID")
    String userId;
    int GuideId;
    Integer reviewId;
    String content;
    LocalDateTime createdDate;
    int score;
}
