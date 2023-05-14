package com.idle.gaza.api.controller;

import com.idle.gaza.api.request.GuideRequest;
import com.idle.gaza.api.response.CodeResponse;
import com.idle.gaza.api.service.CodeService;
import com.idle.gaza.db.entity.Code;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "코드 API", tags = {"Code"})
@RestController
@RequestMapping("/codes")
@Log4j2
public class CodeController {

    @Autowired
    private CodeService codeService;

    @GetMapping()
    @ApiOperation(value = "코드 목록 반환", notes = "해당하는 코드 목록을 보여준다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> getCodeList(@RequestParam("name") String name) {
        List<CodeResponse> getList = codeService.getCode(name);

        if(getList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(getList, HttpStatus.OK);
    }


}