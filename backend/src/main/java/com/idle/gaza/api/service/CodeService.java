package com.idle.gaza.api.service;

import com.idle.gaza.api.response.CodeResponse;
import com.idle.gaza.db.entity.Code;

import java.util.List;

public interface CodeService {

    List<CodeResponse> getCode(String name);


}
