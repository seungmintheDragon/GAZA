package com.idle.gaza.api.controller;

import com.idle.gaza.api.request.*;
import com.idle.gaza.api.response.GuideResponse;
import com.idle.gaza.api.response.LanguageResponse;
import com.idle.gaza.api.response.LocationResponse;
import com.idle.gaza.api.response.ThemaResponse;
import com.idle.gaza.api.service.GuideService;
import com.idle.gaza.common.util.S3Uploader;
import com.idle.gaza.common.util.TokenUtil;
import com.idle.gaza.db.entity.Guide;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(value = "가이드 API", tags = {"Guide"})
@RestController
@RequestMapping("/guides")
@Log4j2
public class GuideController {

    @Autowired
    GuideService guideService;

    @Value("${cloud.aws.directory}")
    String rootPath;

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private TokenUtil tokenUtil;

    private final String locationPath = "/" + "guide" + "/" + "location" + "/";//추천장소 파일 업로드 경로
    private final String myPagePath = "/" + "guide" + "/" + "mypage" + "/";//가이드 마이 페이지 파일 업로드 경로


    //가이드 등록
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "가이드 등록", notes = "가이드를 등록한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> guideRegister(@RequestPart GuideRequest guide, @RequestPart(value = "uploadFile", required = false) MultipartFile multipartFile) {
        log.info("guide = " + guide.toString());
        if (!multipartFile.isEmpty()) {
            String uploadPath = rootPath + myPagePath;

            String fileName = multipartFile.getOriginalFilename();//원본 파일명
            String uploadFileName = UUID.randomUUID().toString() + "_" + fileName;
            log.info("file name = " + uploadFileName);

            try {
                s3Uploader.upload(multipartFile, uploadPath + uploadFileName);
                guide.setPicture(fileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        int result = guideService.guideRegister(guide);

        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    ///////////////////////마이 페이지///////////////////////////////

    //마이페이지 조회
    @GetMapping("/mypage")
    @ApiOperation(value = "마이페이지 조회", notes = "가이드 마이페이지 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> myPageShow(@RequestParam String userId) {
        GuideResponse response = guideService.getMyPage(userId);

        if (response == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        log.info(response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //마이페이지 수정
    @PutMapping(value = "/mypage", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "마이페이지 수정", notes = "가이드 마이페이지 수정")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> myPageModify(@RequestPart MyPageRequest guide, @RequestPart(value = "picture", required = false) MultipartFile multipartFile) {

        log.info("guide " + guide.toString());

        if(multipartFile != null) {
            //파일이 존재한다면 기존 경로에서 파일 삭제
            String originPictureName = guideService.findGuideProfilePicture(guide.getUserId());

            if (originPictureName != null) {
                String originPicture = new String(rootPath + myPagePath + originPictureName);
                log.info("exist file path = " + originPicture);

                try {
                    s3Uploader.deleteS3(originPictureName);
                    log.info("delete file");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //파일 업로드
            String uploadPath = rootPath + myPagePath;
            String fileName = multipartFile.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            try {
                s3Uploader.upload(multipartFile, uploadPath + uploadFileName);
                guide.setPicture(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        int result = guideService.setMyPage(guide);

        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //////////////////////가이드 조회///////////////////////////

    @GetMapping(value = "/search")
    @ApiOperation(value = "검색 창으로 가이드 조회", notes = "나라 또는 도시를 통해 가이드를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> search(@RequestParam String searchName) {
        List<GuideResponse> searchList = guideService.guideSearchBar(searchName);

        if (searchList.size() == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        log.info("search size = " + searchList.size());
        return new ResponseEntity<>(searchList, HttpStatus.OK);
    }


    @GetMapping("/search/thema/")
    @ResponseBody
    @ApiOperation(value = "테마로 가이드 조회", notes = "테마로 가이드 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> guideSearchByThema(@RequestParam String themaName) {
        List<GuideResponse> response = guideService.guideSearchByThema(themaName);
        if (response.size() == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //가이드 전체 조회
    @GetMapping()
    @ApiOperation(value = "가이드 전체 조회", notes = "가이드 전체 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> guideSearch() {
        List<GuideResponse> guideList = guideService.guideSearch();

        if (guideList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(guideList, HttpStatus.OK);
    }

    //인기 가이드 조회
    @GetMapping("/popular")
    @ApiOperation(value = "인기 가이드 조회", notes = "인기 가이드 목록을 조회한다.(예약 많은 순으로 정렬)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<?> popularGuideSearch() {
        try {
            List<GuideResponse> guideList = guideService.famousGuideSearch();
            return new ResponseEntity<>(guideList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    //가이드 상세 프로필 조회
    @GetMapping("/{guideId}")
    @ApiOperation(value = "가이드 상세 프로필 조회", notes = "가이드 프로필을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> guideProfileSearch(@PathVariable @ApiParam(value = "가이드PK", required = true) int guideId) {
        GuideResponse guide = guideService.guideDetailSearch(guideId);

        if (guide == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(guide, HttpStatus.OK);
    }


    ////////////////////////추천 장소/////////////////////////////

    @PostMapping(value = "/location", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "추천 장소 등록", notes = "추천 장소를 등록한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> locationRegister(@RequestPart(name = "location") LocationPostRequest location, @RequestPart(name = "uploadFile", required = false) MultipartFile multipartFile) {
        log.info("location = " + location.toString());

        if (!multipartFile.isEmpty()) {
            //make upload folder
            String uploadPath = rootPath + locationPath;

            String fileName = multipartFile.getOriginalFilename();//원본 파일명
            String uploadFileName = UUID.randomUUID().toString() + "_" + fileName;
            log.info("file name = " + uploadFileName);

            try {
                //upload file
                s3Uploader.upload(multipartFile, uploadPath + uploadFileName);
                location.setPicture(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        int result = guideService.locationRegister(location);
        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/location")
    @ApiOperation(value = "추천 장소 삭제", notes = "추천 장소를 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<?> locationDelete(@RequestParam("loginId") String loginId, @RequestParam("recommendId") String recommendId) {
//        String loginId = map.get("loginId");
        int id = Integer.parseInt(recommendId);

        //파일이 존재한다면 기존 경로에서 파일 삭제
        String existFile = guideService.findExistFile(id);

        if (existFile != null) {
            String existPath = new String(rootPath + locationPath + existFile);
            log.info("exist file path = " + existPath);

            try {
                s3Uploader.deleteS3(existPath);
                log.info("delete file");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        guideService.locationDelete(loginId, id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(name = "/location", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "추천 장소 수정", notes = "추천 장소를 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<?> locationUpdate(@RequestPart LocationPostRequest location, @RequestPart(name = "uploadFile", required = false) MultipartFile multipartFile) {

        //기존 경로에서 사진 삭제
        String existFile = guideService.findExistFile(location.getRecommendId());
        if (existFile != null) {
            String originalFile = new String(rootPath + locationPath + existFile);
            log.info("exist file path = " + originalFile);

            try {
                s3Uploader.deleteS3(originalFile);
                log.info("delete file");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //수정한 사진 업로드
        if (!multipartFile.isEmpty()) {
            String uploadPath = rootPath + locationPath;

            String fileName = multipartFile.getOriginalFilename();//원본 파일명
            String uploadFileName = UUID.randomUUID().toString() + "_" + fileName;
            log.info("file name = " + uploadFileName);

            try {
                s3Uploader.upload(multipartFile, uploadPath + uploadFileName);
                location.setPicture(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        int result = guideService.locationUpdate(location);
        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 추천 장소 조회
    @GetMapping("/location/{guideId}")
    @ApiOperation(value = "추천 장소 조회", notes = "추천 장소를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> locationSearch(@PathVariable @ApiParam(value = "가이드의 유저 아이디(String)", required = true) String guideId) {
        List<LocationResponse> list = guideService.locationSearch(guideId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    /////////////////////여행 테마 기능///////////////////////////

    @PostMapping("/thema")
    @ApiOperation(value = "여행 테마 등록", notes = "여행 테마를 등록한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<?> tourThemaRegister(@RequestParam @ApiParam(value = "login id", required = true) String loginId, @RequestParam(name = "thema", required = false) @ApiParam(value = "테마이름", required = true) String themaName) {
        int result = guideService.themaRegister(loginId, themaName);

        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/thema")
    @ApiOperation(value = "여행 테마 삭제", notes = "가이드가 등록한 여행 테마를 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> tourThemaDelete(@RequestParam @ApiParam(value = "login id", required = true) String loginId, @RequestParam int themaId) {
        int result = guideService.themaDelete(loginId, themaId);

        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/thema")
    @ApiOperation(value = "가이드의 여행 테마 조회", notes = "가이드의 여행 테마를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> tourThemaShow(@RequestParam @ApiParam(value = "로그인 아이디", required = true) String loginId) {
        List<ThemaResponse> results = guideService.themaSelect(loginId);

        if (results == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }


    //////////////////상담 시간 관리////////////////////////

    @PostMapping("/day")
    @ApiOperation(value = "상담 불가능한 날짜 등록", notes = "가이드는 상담 불가능한 날짜를 등록한다. String userId와 LocalDate day을 받음")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<?> dayRegister(@RequestBody DayOffPostRequest dayOff) {
        LocalDate day = dayOff.getDay();
        String userId = dayOff.getUserId();

        log.info("day" + day);
        log.info("userId" + userId);

        int result = guideService.consultDateRegister(userId, day);
        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/day")
    @ApiOperation(value = "상담 불가능한 날짜 취소", notes = "가이드는 상담 불가능한 날짜를 취소한다. 날짜 형식은 2022-01-20")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> dayDelete(@RequestBody @ApiParam(value = "map의 key는 userId와 dayId") Map<String, String> map) {
        //log.info("userId = " + map.get("userId") + ", dayId = " + map.get("dayId"));

        String userId = map.get("userId");
        int dayId = Integer.parseInt(map.get("dayId"));

        int result = guideService.consultDateDelete(userId, dayId);
        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/time")
    @ApiOperation(value = "상담 불가능한 시간대 등록", notes = "가이드는 상담 불가능한 시작, 종료 시간을 등록한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> timeRegister(@RequestBody TimeRegisterPostRequest time) {

        int result = guideService.consultTimeRegister(LocalTime.parse(time.getTimeStart()), LocalTime.parse(time.getTimeEnd()), time.getUserId());
        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/time")
    @ApiOperation(value = "상담 불가능한 시간대 삭제", notes = "가이드는 상담 불가능한 시작, 종료 시간을 삭제한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> timeDelete(@RequestParam @ApiParam(value = "loginId") String loginId) {
        int result = guideService.consultTimeDelete(loginId);
        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //////////////////////// 가이드 언어 관리 기능 //////////////////////////////////

    @PostMapping("/lang")
    @ApiOperation(value = "가이드 사용 가능한 언어 등록", notes = "가이드는 사용 가능한 언어를 등록한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> languageRegister(@RequestBody LanguageRequest request) {

        int result = guideService.languageRegister(request);

        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/lang")
    @ApiOperation(value = "가이드 사용 가능한 언어 삭제", notes = "가이드는 사용 가능한 언어를 삭제한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> languageDelete(@RequestParam String userId, @RequestParam int langId) {
        int result = guideService.languageDelete(userId, langId);

        if (result == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/lang")
    @ApiOperation(value = "가이드 사용 가능한 언어 조회", notes = "가이드는 사용 가능한 언어를 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류"),
            @ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<?> getLanaguage(@RequestParam String userId) {
        List<LanguageResponse> lang = guideService.getLanguage(userId);
        if (lang == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(lang, HttpStatus.OK);
    }


}
