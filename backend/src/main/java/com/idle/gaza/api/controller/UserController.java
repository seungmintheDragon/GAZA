package com.idle.gaza.api.controller;

import com.idle.gaza.api.dto.TokenDto;
import com.idle.gaza.api.request.UserUpdateRequest;
import com.idle.gaza.api.response.GuideDocumentResponse;
import com.idle.gaza.api.service.UserService;
import com.idle.gaza.common.codes.SuccessCode;
import com.idle.gaza.common.response.ApiResponse;
import com.idle.gaza.common.util.S3Uploader;
import com.idle.gaza.common.util.TokenUtil;
import com.idle.gaza.db.entity.GuideDocument;
import com.idle.gaza.db.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*")
@Api(value = "유저 API", tags = {"User"})
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private S3Uploader s3Uploader;

    @Value("${cloud.aws.directory}")
    String rootPath;

    @PostMapping(value = "",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "회원가입", notes = "회원가입")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "성공"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 오류"),
            @io.swagger.annotations.ApiResponse(code = 204, message = "사용자 없음")
    })

    public ResponseEntity<ApiResponse<Object>> join(@RequestPart(value = "user") User user, @RequestPart(value = "picture", required = false) MultipartFile pictureFile) {
        if (pictureFile != null) {
            //make upload folder
            String uploadPath = rootPath + "/" + "user" + "/" + "picture" + "/";
            File uploadFilePath = new File(rootPath, uploadPath);

            String fileName = pictureFile.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            log.info("file name = " + uploadFileName);

            try {
                log.debug(s3Uploader.upload(pictureFile, uploadPath + uploadFileName));
                user.setPicture(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userService.join(user);

        ApiResponse<Object> ar = ApiResponse.builder()
                .result(user)
                .resultCode(SuccessCode.INSERT.getStatus())
                .resultMsg(SuccessCode.INSERT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    /**
     * [API] 가이드 신청 리스트
     *
     * @return ResponseEntity
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> checkDuplicated(@PathVariable String id) {
        User user = userService.searchUserById(id);

        if (user != null) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(user)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("중복된 아이디입니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(SuccessCode.SELECT.getStatus())
                    .resultMsg("사용 가능한 아이디입니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }
    }

    /**
     * [API] 비밀 번호 찾기
     *
     * @param user User
     * @return ResponseEntity
     * <p>
     * 이메일 통한 방법이라고 했는데 보류
     */
    @PostMapping("/pw")
    public ResponseEntity<ApiResponse<Object>> findPassword(@RequestBody User user) {

        ApiResponse<Object> ar = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    /**
     * [API] 사용자 내 정보 조회
     *
     * @param accessToken String
     * @return ResponseEntity
     *
     * header에서 토큰 가져와서 유저 정보 가져와서 보여주기
     */
    @GetMapping("")
    @ApiOperation(value = "사용자 조회", notes = "사용자를 조회한다.")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "성공"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 오류"),
            @io.swagger.annotations.ApiResponse(code = 204, message = "사용자 없음")
    })
    public ResponseEntity<ApiResponse<Object>> getUser(@RequestHeader("Authorization") String accessToken) {
        log.debug("accessToken = " + accessToken);
        
        String token = tokenUtil.getTokenFromHeader(accessToken);

        String id = tokenUtil.getUserIdFromToken(token);

        User user = userService.searchUser(id);

        if (user == null) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("회원이 없습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(user)
                    .resultCode(SuccessCode.SELECT.getStatus())
                    .resultMsg("회원이 조회되었습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }
    }

    /**
     * [API] 사용자 정보 수정
     *
     * @param userUpdateRequest UserUpdateRequest
     * @return ResponseEntity
     */
    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보를 수정한다.")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "성공"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 오류"),
            @io.swagger.annotations.ApiResponse(code = 204, message = "사용자 없음")
    })
    @PutMapping("")
    public ResponseEntity<ApiResponse<Object>> updateUser(@RequestHeader("Authorization") String accessToken, @RequestPart(value = "userInfo") UserUpdateRequest userUpdateRequest, @RequestPart(value = "picture", required = false) MultipartFile pictureFile) {
        String token = tokenUtil.getTokenFromHeader(accessToken);

        String id = tokenUtil.getUserIdFromToken(token);


        if(pictureFile != null) {
            //파일이 존재한다면 기존 경로에서 파일 삭제
            User user = userService.searchUser(id);

            String originPictureName = user.getPicture();

            if (originPictureName != null) {
                String originPicture = new String(rootPath + "/" + "user" + "/" + "picture" + "/" + originPictureName);
                log.info("exist file path = " + originPicture);

                try {
                    s3Uploader.deleteS3(originPictureName);
                    log.info("delete file");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String uploadPath = rootPath + "/" + "user" + "/" + "picture" + "/";
            File uploadFilePath = new File(rootPath, uploadPath);

            String fileName = pictureFile.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            try {
                s3Uploader.upload(pictureFile, uploadPath + uploadFileName);
                userUpdateRequest.setPicture(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        int result = userService.updateUser(id, userUpdateRequest);

        log.debug("========================= result = " + result);

        if (result == 0) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("회원이 없습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(SuccessCode.UPDATE.getStatus())
                    .resultMsg("회원이 수정되었습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }
    }

    /**
     * [API] 회원 탈퇴
     *
     * @param accessToken String
     * @return ResponseEntity
     */
    @DeleteMapping("")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@RequestHeader("Authorization") String accessToken) {
        String token = tokenUtil.getTokenFromHeader(accessToken);

        String id = tokenUtil.getUserIdFromToken(token);

        int result = userService.deleteUser(id);

        if (result == 0) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("회원이 없습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(SuccessCode.DELETE.getStatus())
                    .resultMsg("회원이 탈퇴되었습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }
    }

    /**
     * [API] 비밀번호 수정
     *
     * @param accessToken String
     * @param passwordMap Map<String, String>
     * @return ResponseEntity
     */
    @PutMapping("/pw")
    public ResponseEntity<ApiResponse<Object>> changePassword(@RequestHeader("Authorization") String accessToken, @RequestBody Map<String, String> passwordMap) {
        String token = tokenUtil.getTokenFromHeader(accessToken);

        String id = tokenUtil.getUserIdFromToken(token);

        int result = userService.updatePassword(id, passwordMap.get("password"));

        if (result == 0) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("회원이 없습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(SuccessCode.UPDATE.getStatus())
                    .resultMsg("비밀번호가 변경되었습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }
    }

    /**
     * [API] 사용자 로그인 상태 조회
     *
     * @return ResponseEntity
     * <p>
     * 프론트에서 만료 시간을 가지고 있다면 자체적으로 확인 가능할듯(만료X면 그대로 사용, 만료됐으면 auth/reissue
     */
    @PostMapping("/isLogin")
    public ResponseEntity<ApiResponse<Object>> checkLogin() {

        ApiResponse<Object> ar = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    /**
     *
     * @param accessToken String
     * @param idFileFile MultipartFile
     * @param certificateResidenceFile MultipartFile
     * @param certificateFile MultipartFile
     * @return
     */
    @PostMapping("/guide")
    public ResponseEntity<ApiResponse<Object>> joinGuide(@RequestHeader("Authorization") String accessToken,
                                                         @RequestParam("idFile") MultipartFile idFileFile,
                                                         @RequestParam("certificateResidence") MultipartFile certificateResidenceFile,
                                                         @RequestParam("certificate") MultipartFile certificateFile) {

        String token = tokenUtil.getTokenFromHeader(accessToken);

        String id = tokenUtil.getUserIdFromToken(token);

        if (idFileFile == null || certificateResidenceFile == null) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("파일을 확인해주세요.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        }

        int updateResult = userService.changeState(id, "US3");

        if (updateResult == 0) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("회원이 없습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        }

        //make upload folder
        String guideFileUploadPath = "/" + "guide_document" + "/";

        String idFileUploadPath = rootPath + guideFileUploadPath + "id_file" + "/";
        String certificateResidenceUploadPath = rootPath + guideFileUploadPath + "certificate_residence" + "/";

        File idFileUploadFilePath = new File(rootPath, idFileUploadPath);
        File certificateResidenceUploadFilePath = new File(rootPath, certificateResidenceUploadPath);

        if (!idFileUploadFilePath.exists()) {
            idFileUploadFilePath.mkdirs();
        }

        if (!certificateResidenceUploadFilePath.exists()) {
            certificateResidenceUploadFilePath.mkdirs();
        }

        String idFileFileName = idFileFile.getOriginalFilename();
        String certificateResidenceFileName = certificateResidenceFile.getOriginalFilename();


        UUID uuid = UUID.randomUUID();
        String idFileUploadFileName = uuid.toString() + "_" + idFileFileName;
        String certificateResidenceUploadFileName = uuid.toString() + "_" + certificateResidenceFileName;

        try {
            s3Uploader.upload(idFileFile, idFileUploadPath + idFileUploadFileName);
            s3Uploader.upload(certificateResidenceFile, certificateResidenceUploadPath + certificateResidenceUploadFileName);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 가이드 문서에 넣어주기
        GuideDocument guideDocument = GuideDocument.builder()
                .idFile(idFileUploadFileName)
                .certificateResidence(certificateResidenceUploadFileName)
                .build();

        if (certificateFile != null) {
            String certificateUploadPath = rootPath + guideFileUploadPath + "certificate" + "/";

            File certificateUploadFilePath = new File(rootPath, certificateUploadPath);

            if (!certificateUploadFilePath.exists()) {
                certificateUploadFilePath.mkdirs();
            }

            String certificateFileName = certificateFile.getOriginalFilename();

            String certificateUploadFileName = uuid.toString() + "_" + certificateFileName;

            try {
                s3Uploader.upload(certificateFile, certificateUploadPath + certificateUploadFileName);
                guideDocument.setCertificate(certificateUploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        int insertResult = userService.registerGuide(id, guideDocument);
        userService.changeState(id, "US3");

        if (insertResult == 0) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("회원이 없습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(SuccessCode.INSERT.getStatus())
                    .resultMsg("가이드 신청되었습니다.").build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }
    }

    /**
     * [API] 가이드 신청 리스트
     *
     * @return ResponseEntity
     *
     */
    @GetMapping("/guide")
    public ResponseEntity<ApiResponse<Object>> getGuideRegisterList() {
        List<GuideDocumentResponse> list = userService.searchGuideRegisterList();

        if (list == null) {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(null)
                    .resultCode(HttpStatus.NO_CONTENT.value())
                    .resultMsg("신청자가 없습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<Object> ar = ApiResponse.builder()
                    .result(list)
                    .resultCode(SuccessCode.SELECT.getStatus())
                    .resultMsg("리스트가 조회되었습니다.")
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }
    }

    @PostMapping("test")
    public void test(HttpServletRequest request){
        Enumeration params = request.getParameterNames();
        log.debug("----------------------------");
        while (params.hasMoreElements()){
            String name = (String)params.nextElement();
            log.debug(name + " : " +request.getParameter(name));
        }
        log.debug("----------------------------");
    }

    /**
     * [API] 로그아웃
     *
     * @param tokenDto TokenDto
     * @return ResponseEntity
     *
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(@RequestBody TokenDto tokenDto) {
        tokenUtil.logout(tokenDto);

        ApiResponse<Object> ar = ApiResponse.builder()
                .result(null)
                .resultCode(200)
                .resultMsg("로그아웃 되었습니다.")
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}
