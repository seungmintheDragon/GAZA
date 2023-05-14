package com.idle.gaza.api.controller;

import com.idle.gaza.api.request.TravelRouteRequest;
import com.idle.gaza.api.response.TravelRouteResponse;
import com.idle.gaza.api.service.TravelRouteServiceImpl;
import com.idle.gaza.common.model.response.BaseResponseBody;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Log4j2
@Api(value = "여행일정 API", tags = {"Travel Route"})
@RestController
@RequestMapping("/routes")
public class TravelRouteController {

    @Autowired
    TravelRouteServiceImpl travelRouteService;

    @PostMapping("/{reservationId}")
    @ApiOperation(value = "루트 작성", notes = "루트를 저장한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> saveRoutes(@PathVariable @ApiParam(value="예약 PK") int reservationId,
                                                                 @RequestBody @ApiParam(value="루트 정보") List<TravelRouteRequest> travelInfo){
        try{
            travelRouteService.saveRoutes(travelInfo, reservationId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, "fail"));
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    @GetMapping("/{reservationId}")
    @ApiOperation(value = "여행 일정 조회", notes = "여행 일정을 조회 할 수 있다.")
    public ResponseEntity<?> getRoutes(@PathVariable int reservationId){
        List<TravelRouteResponse> routes = travelRouteService.getRoutes(reservationId);
        return new ResponseEntity<List<?>>(routes, HttpStatus.OK);
    }

    //엑셀 추출을 위한 여행 일정 조회(정렬)
    @GetMapping("/excel/{reservationId}")
    public ResponseEntity<?> getRouteByOrder(@PathVariable String reservationId, HttpServletResponse response){
        int parseIntId = Integer.parseInt(reservationId);
        log.info("id = "+ parseIntId);
        int result = travelRouteService.excelDownload(parseIntId, response);
        if(result == 0) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
