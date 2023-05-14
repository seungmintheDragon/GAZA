package com.idle.gaza.api.controller;

import com.idle.gaza.api.dto.TokenDto;
import com.idle.gaza.api.request.GuideRequest;
import com.idle.gaza.api.service.GuideService;
import com.idle.gaza.api.service.UserService;
import com.idle.gaza.common.codes.SuccessCode;
import com.idle.gaza.common.response.ApiResponse;
import com.idle.gaza.common.util.TokenUtil;
import com.idle.gaza.db.entity.Guide;
import com.idle.gaza.db.entity.User;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "관리자 API", tags = {"Admin"})
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private GuideService guideService;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * [API] 가이드 신청 승인
     *
     * @param id String
     * @return ResponseEntity
     *
     *
     */
    @PutMapping("/guide/success")
    public ResponseEntity<ApiResponse<Object>> acceptGuide(@RequestBody Map<String, String> idMap) {
        String id = idMap.get("id");

        userService.changeState(id, "US1");

        User user = userService.searchUserById(id);

        if(user != null) {
            GuideRequest guideRequest = new GuideRequest();

            guideRequest.setId(id);

            guideService.guideRegister(guideRequest);

            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(SuccessCode.UPDATE.getStatus())
                    .resultMsg(SuccessCode.UPDATE.getMessage())
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(SuccessCode.UPDATE.getStatus())
                    .resultMsg("가이드 승인에 실패했습니다")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * [API] 가이드 신청 거절
     *
     * @param id String
     * @return ResponseEntity
     *
     *
     */
    @PutMapping("/guide/failure")
    public ResponseEntity<ApiResponse<Object>> rejectGuide(@RequestBody Map<String, String> idMap) {
        String id = idMap.get("id");

        userService.changeState(id, "US2");

        ApiResponse<Object> ar = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.UPDATE.getStatus())
                .resultMsg(SuccessCode.UPDATE.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}