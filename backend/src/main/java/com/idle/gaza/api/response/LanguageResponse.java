package com.idle.gaza.api.response;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("LanaguageResponse")
@Getter
@Setter
@NoArgsConstructor
public class LanguageResponse {

    private int language_id;
    //private int guide_id;
    //private String lang_code;
    private String languageName;

}
