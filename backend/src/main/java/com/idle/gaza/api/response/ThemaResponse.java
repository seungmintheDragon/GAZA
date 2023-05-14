package com.idle.gaza.api.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ThemaResponse")
public class ThemaResponse {

    private String themaCode;
    private String themaName;
    private int guideId;
    private int themaId;

    @Builder
    public ThemaResponse(String themaCode, String themaName, int guideId, int themaId) {
        this.themaCode = themaCode;
        this.themaName = themaName;
        this.guideId = guideId;
        this.themaId = themaId;
    }
}
