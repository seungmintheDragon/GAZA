package com.idle.gaza.api.service;

import com.idle.gaza.api.response.CodeResponse;
import com.idle.gaza.db.entity.Code;
import com.idle.gaza.db.repository.CodeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeRepository codeRepository;

    @Override
    public List<CodeResponse> getCode(String name) {
        List<Code> getList = codeRepository.getCodeList(name);
        List<CodeResponse> response = new ArrayList<>();

        for(Code c : getList){
            CodeResponse res = CodeResponse
                    .builder()
                    .codeId(c.getCodeId())
                    .name(c.getName())
                    .description(c.getDescription())
                    .parentId(c.getParentId())
                    .build();
            response.add(res);
        }
        return response;
    }

}
