package com.idle.gaza.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ConsultingCreateRequest")
public class ConsultingCreateRequest {
    Integer reservationId;
    String url;

}
