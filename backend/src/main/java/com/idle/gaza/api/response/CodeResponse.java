package com.idle.gaza.api.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CodeResponse")
public class CodeResponse {
    private int codeId;

    private int parentId;

    private String name;

    private String description;

    @Builder
    public CodeResponse(int codeId, int parentId, String name, String description) {
        this.codeId = codeId;
        this.parentId = parentId;
        this.name = name;
        this.description = description;
    }
}
