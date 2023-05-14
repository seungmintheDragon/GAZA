package com.idle.gaza.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ReviewCreateRequest")
public class ReviewCreatePostRequest {
    private int reservationId;

    @ApiModelProperty(name="리뷰 내용", example="정말 좋아요")
    private String content;

    private int score;
}
