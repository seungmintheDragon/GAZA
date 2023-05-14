package com.idle.gaza.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("LanguageRequest")
public class LanguageRequest {
    private String loginId;
    private String languageName;

}
