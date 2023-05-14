package com.idle.gaza.api.service;

import com.idle.gaza.api.request.GuideRequest;
import com.idle.gaza.api.request.LanguageRequest;
import com.idle.gaza.api.request.LocationPostRequest;
import com.idle.gaza.api.request.MyPageRequest;
import com.idle.gaza.api.response.GuideResponse;
import com.idle.gaza.api.response.LanguageResponse;
import com.idle.gaza.api.response.LocationResponse;
import com.idle.gaza.api.response.ThemaResponse;
import com.idle.gaza.db.entity.Guide;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface GuideService {

    //가이드 조회 기능
    List<GuideResponse> guideSearch();
    List<GuideResponse> famousGuideSearch();
    GuideResponse guideDetailSearch(int guideId);
    List<GuideResponse> guideSearchBar(String searchName);
    List<GuideResponse> guideSearchByThema(String searchName);

    //가이드 추천 장소 기능
    int locationRegister(LocationPostRequest locations);
    int locationDelete(String loginId, int recommendId);
    int locationUpdate(LocationPostRequest locations);
    List<LocationResponse> locationSearch(String guideId);

    String findExistFile(int recommendId);
    String findGuideProfilePicture(String loginId);

    //상담 날짜 관리 기능
    int consultDateRegister(String userId, LocalDate dayoff);
    int consultDateDelete(String userId, int dayOffId);

    //상담 시간대 관리 기능
    int consultTimeRegister(LocalTime startTime, LocalTime endTime, String userId);
    int consultTimeDelete(String loginId);

    //가이드 사용 언어 관리 기능
    int languageRegister(LanguageRequest request);
    int languageDelete(String loginId, int langId);
    List<LanguageResponse> getLanguage(String loginId);

    //여행 테마 관리 기능
    int themaRegister(String loginId, String themaName);
    int themaDelete(String loginId, int themaId);
    List<ThemaResponse> themaSelect(String loginId);



    //가이드 등록
    int guideRegister(GuideRequest guide);


    //가이드 마이페이지 기능
    GuideResponse getMyPage(String loginId);
    int setMyPage(MyPageRequest request);

}
