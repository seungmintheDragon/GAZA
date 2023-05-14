package com.idle.gaza.api.service;

import com.idle.gaza.api.request.GuideRequest;
import com.idle.gaza.api.request.LanguageRequest;
import com.idle.gaza.api.request.LocationPostRequest;
import com.idle.gaza.api.request.MyPageRequest;
import com.idle.gaza.api.response.*;
import com.idle.gaza.db.entity.*;
import com.idle.gaza.db.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Guide 관련 비스니스 로직 처리를 위한 서비스 클래스
 * */
@Service("guideService")
@Log4j2
public class GuideServiceImpl implements GuideService {

    @Autowired
    GuideRecommendRepository guideRecommendRepository;

    @Autowired
    GuideRepository guideRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DayOffRepository dayOffRepository;

    @Autowired
    GuideThemaRepository guideThemaRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    LanguageRepository languageRepository;


    ///////////////////////가이드 조회 기능/////////////////////////

    @Override
    public List<GuideResponse> guideSearch() {
        List<Guide> guideList = guideRepository.findBy();
        List<GuideResponse> searchList = new ArrayList<>();

        for (Guide guide : guideList) {
            List<String> langList = new ArrayList<>();

            for (GuideLanguage guideLanguage : guide.getLanguageList()) {//가이드 언어 테마 가져옴
                String code = guideRepository.searchNameByCode(guideLanguage.getLangCode());
                langList.add(code);
            }

            List<String> themaList = new ArrayList<>();
            for (GuideThema thema : guide.getGuideThemaList()) {
                String code = guideRepository.searchNameByCode(thema.getThemaCode());
                themaList.add(code);
            }

            GuideResponse res = GuideResponse.builder()
                    .guideId(guide.getGuideId())
                    .city(guide.getCity())
                    .country(guide.getCountry())
                    .closeTimeEnd(guide.getCloseTimeEnd())
                    .closeTimeStart(guide.getCloseTimeStart())
                    .introduction(guide.getIntroduction())
                    .onelineIntroduction(guide.getOnlineIntroduction())
                    .picture(guide.getPicture())
                    .name(guide.getUser().getName())
                    .price(guide.getPrice())
                    .userId(guide.getUser().getUserId())
                    .gender(guide.getUser().getGender())
                    .language(langList)
                    .thema(themaList)
                    .build();

            searchList.add(res);
        }

        return searchList;
    }

    @Override
    public List<GuideResponse> guideSearchBar(String searchName) {
        List<Guide> guideList = guideRepository.searchByCountryOrCity(searchName);
        List<GuideResponse> searchList = new ArrayList<>();

        for (Guide guide : guideList) {
            List<String> codeList = new ArrayList<>();
            for (GuideLanguage guideLanguage : guide.getLanguageList()) {
                String code = guideRepository.searchNameByCode(guideLanguage.getLangCode());
                codeList.add(code);
            }

            List<String> themaList = new ArrayList<>();
            for (GuideThema thema : guide.getGuideThemaList()) {
                String code = guideRepository.searchNameByCode(thema.getThemaCode());
                themaList.add(code);
            }


            GuideResponse res = GuideResponse.builder().guideId(guide.getGuideId())
                    .city(guide.getCity())
                    .country(guide.getCountry())
                    .closeTimeEnd(guide.getCloseTimeEnd())
                    .closeTimeStart(guide.getCloseTimeStart())
                    .introduction(guide.getIntroduction())
                    .onelineIntroduction(guide.getOnlineIntroduction())
                    .picture(guide.getPicture())
                    .name(guide.getUser().getName())
                    .price(guide.getPrice())
                    .userId(guide.getUser().getUserId())
                    .thema(themaList)
                    .language(codeList)
                    .gender(guide.getUser().getGender())

                    .build();
            searchList.add(res);
        }

        return searchList;
    }

    @Override
    public List<GuideResponse> famousGuideSearch() {
        //예약 많은 순으로 정렬
        List<Guide> orderByGuide = guideRepository.findOrderByMaxReservation();

        List<GuideResponse> guideResponseList = new ArrayList<>();
        for (Guide guide : orderByGuide) {
            List<String> codeList = new ArrayList<>();
            for (GuideLanguage guideLanguage : guide.getLanguageList()) {
                String code = guideRepository.searchNameByCode(guideLanguage.getLangCode());
                codeList.add(code);
            }

            List<String> themaList = new ArrayList<>();
            for (GuideThema thema : guide.getGuideThemaList()) {
                String code = guideRepository.searchNameByCode(thema.getThemaCode());
                themaList.add(code);
            }

            GuideResponse guideResponse = GuideResponse.builder()
                    .guideId(guide.getGuideId())
                    .name(guide.getUser().getName())
                    .city(guide.getCity())
                    .closeTimeEnd(guide.getCloseTimeEnd())
                    .closeTimeStart(guide.getCloseTimeStart())
                    .gender(guide.getUser().getGender())
                    .country(guide.getCountry())
                    .price(guide.getPrice())
                    .picture(guide.getPicture())
                    .language(codeList)
                    .thema(themaList)
                    .build();
            guideResponseList.add(guideResponse);


        }

        return guideResponseList;
    }

    @Override
    public GuideResponse guideDetailSearch(int guideId) {

        //가이드 정보 리턴
        Optional<Guide> guide = guideRepository.findGuideByGuideId(guideId);
        if (!guide.isPresent()) return null;

        Guide existGuide = guide.get();

        List<String> langList = new ArrayList<>();

        for (GuideLanguage guideLanguage : existGuide.getLanguageList()) {//가이드 언어 테마 가져옴
            String code = guideRepository.searchNameByCode(guideLanguage.getLangCode());
            langList.add(code);
        }

        List<String> themaList = new ArrayList<>();
        for (GuideThema thema : existGuide.getGuideThemaList()) {
            String code = guideRepository.searchNameByCode(thema.getThemaCode());
            themaList.add(code);
        }

        //추천장소
        List<GuideRecommendLocation> rec = existGuide.getGuideLocationList();
        List<LocationResponse> newLocList = new ArrayList<>();
        for (GuideRecommendLocation location : rec) {
            LocationResponse loc = LocationResponse.builder()
                    .address(location.getAddress())
                    .name(location.getName())
                    .longitude(location.getLongitude())
                    .categoryCode(location.getCategoryCode())
                    .picture(location.getPicture())
                    .latitude(location.getLatitude())
                    .build();
            newLocList.add(loc);
        }

        //예약
        List<Reservation> res = existGuide.getReservationList();
        List<ReservationResponse> newResList = new ArrayList<>();
        for (Reservation r : res) {
            ReservationResponse reservation = ReservationResponse
                    .builder()
                    .reservationId(r.getReservationId())
                    .consultingDate(r.getConsultingDate())
                    .note(r.getNote())
                    .numberOfPeople(r.getNumberOfPeople())
                    .stateCode(r.getStateCode())
                    .travelEndDate(r.getTravelEndDate())
                    .travelStartDate(r.getTravelStartDate())
                    .withChildren(r.getWithChildren())
                    .withDisabled(r.getWithDisabled())
                    .withElderly(r.getWithElderly())
                    .build();
            newResList.add(reservation);
        }

        List<DayOff> days = existGuide.getDayOffList();
        List<DayOffResponse> dayOffResponseList = new ArrayList<>();
        for (DayOff d : days) {
            DayOffResponse dayOffResponse = DayOffResponse
                    .builder()
                    .day(d.getDayOffDate())
                    .guideId(d.getGuide().getGuideId())
                    .dayOffId(d.getDayOffId())
                    .build();
            dayOffResponseList.add(dayOffResponse);
        }

        GuideResponse response = GuideResponse.builder()
                .reservationList(newResList)
                .guideLocationList(newLocList)
                .closeTimeStart(existGuide.getCloseTimeStart())
                .closeTimeEnd(existGuide.getCloseTimeEnd())
                .dayOffList(dayOffResponseList)
                .picture(existGuide.getPicture())
                .license(existGuide.getLicense())
                .guideId(existGuide.getGuideId())
                .price(existGuide.getPrice())
                .introduction(existGuide.getIntroduction())
                .country(existGuide.getCountry())
                .city(existGuide.getCity())
                .name(existGuide.getUser().getName())
                .gender(existGuide.getUser().getGender())
                .language(langList)
                .thema(themaList)
                .onelineIntroduction(existGuide.getOnlineIntroduction())
                .id(existGuide.getUser().getId())
                .build();


        return response;
    }

    @Override
    public List<GuideResponse> guideSearchByThema(String searchName) {
        List<GuideResponse> guideResponseList = new ArrayList<>();

        String code = getCode(searchName);
        log.info("code = " + code);
        List<Guide> guideList = guideRepository.searchByCode(code);//해당 테마에 해당하는 가이드 리스트 추출
        log.info("size = " + guideList.size());

        for (Guide guide : guideList) {
            List<String> langList = new ArrayList<>();
            for (GuideLanguage guideLanguage : guide.getLanguageList()) {//해당 가이드의 언어 리스트 가져옴
                String codeName = guideRepository.searchNameByCode(guideLanguage.getLangCode());
                langList.add(codeName);
            }

            List<String> themaList = new ArrayList<>();
            for (GuideThema thema : guide.getGuideThemaList()) {//해당 가이드의 여행 테마를 가져옴
                String codeName = guideRepository.searchNameByCode(thema.getThemaCode());
                themaList.add(codeName);
            }

            GuideResponse guideResponse = GuideResponse.builder()
                    .guideId(guide.getGuideId())
                    .name(guide.getUser().getName())
                    .city(guide.getCity())
                    .country(guide.getCountry())
                    .price(guide.getPrice())
                    .picture(guide.getPicture())
                    .language(langList)
                    .thema(themaList)
                    .build();
            guideResponseList.add(guideResponse);
        }

        return guideResponseList;

    }


    ////////////////////////추천 장소 기능//////////////////////
    @Override
    public int locationRegister(LocationPostRequest locations) {
        //해당 가이드가 존재하는지 확인함
        Optional<User> user = userRepository.findById(locations.getLoginId());
        if (!user.isPresent()) return 0;

        Optional<Guide> guide = guideRepository.findGuideByUser(user.get().getUserId());
        if (!guide.isPresent()) return 0;

        String code = getCode(locations.getCategoryName());//해당 테마의 코드 번호 가져옴
        log.info(code);
        if (code == null) return 0;

        //추천 장소 등록

        GuideRecommendLocation loc = GuideRecommendLocation
                .builder()
                .guide(guide.get())
                .address(locations.getAddress())
                .categoryCode(code)
                .picture(locations.getPicture())
                .name(locations.getName())
                .build();
        log.info("loc = " + loc.toString());
        guideRecommendRepository.save(loc);

        return 1;
    }

    @Override
    public int locationDelete(String loginId, int recommendId) {
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return 0;

        Optional<Guide> existGuide = guideRepository.findGuideByUser(user.get().getUserId());
        if (!existGuide.isPresent()) return 0;

        //추천 장소를 삭제한다.
        int result = guideRecommendRepository.delRecommendLocation(recommendId, existGuide.get().getGuideId());
        return result;
    }

    @Override
    public int locationUpdate(LocationPostRequest locations) {
        //해당 가이드가 존재하는지 확인함
        Optional<User> user = userRepository.findById(locations.getLoginId());
        if (!user.isPresent()) return 0;

        Optional<Guide> existGuide = guideRepository.findGuideByUser(user.get().getUserId());
        if (existGuide.isPresent()) return 0;


        //해당 가이드 추천장소가 존재하는 경우에만 수행
        Optional<GuideRecommendLocation> existLocation = guideRecommendRepository.findByRecommendId(locations.getRecommendId());

        if (!existLocation.isPresent()) return 0;

        String code = getCode(locations.getCategoryName());
        GuideRecommendLocation updateLocation = existLocation.get();
        updateLocation.setGuide(existGuide.get());
        updateLocation.setAddress(locations.getAddress());
        updateLocation.setName(locations.getName());
        updateLocation.setPicture(locations.getPicture());
        updateLocation.setCategoryCode(code);

        guideRecommendRepository.save(updateLocation);

        return 1;//성공 시
    }

    @Override
    public List<LocationResponse> locationSearch(String guideId) {
        List<GuideRecommendLocation> locations = guideRecommendRepository.findByGuide_UserId_Id(guideId);
        List<LocationResponse> locationRes = new ArrayList<>(locations.size());
        for (int i = 0; i < locations.size(); i++) {
            GuideRecommendLocation location = locations.get(i);
            LocationResponse res = LocationResponse
                    .builder()
                    .locationId(location.getRecommendId())
                    .address(location.getAddress())
                    .name(location.getName())
                    .categoryCode(location.getCategoryCode())
                    .longitude(location.getLongitude())
                    .latitude(location.getLatitude())
                    .picture(location.getPicture())
                    .build();
            locationRes.add(res);
        }
        return locationRes;
    }

    @Override
    public String findExistFile(int recommendId) {
        Optional<GuideRecommendLocation> loc = guideRecommendRepository.findByRecommendId(recommendId);
        String file = loc.get().getPicture();

        return file;
    }

    //////////////////가이드 등록////////////////////////////

    @Override
    public int guideRegister(GuideRequest guide) {
        //해당 회원이 존재하는지 확인함
        Optional<User> checkId = userRepository.findById(guide.getId());
        if (!checkId.isPresent()) return 0;

        Optional<User> user = userRepository.findByUserId(checkId.get().getUserId());
        if (!user.isPresent()) return 0;

        //가이드가 이미 존재하는 경우
        Optional<Guide> existGuide = guideRepository.findGuideByUser(user.get().getUserId());
        if (existGuide.isPresent()) return 0;

        /*
        Guide newGuide = Guide.builder()
                .user(user.get())
                .picture(guide.getPicture())
                .price(guide.getPrice())
                .license(guide.getLicense())
                .onlineIntroduction(guide.getOnlineIntroduction())
                .introduction(guide.getIntroduction())
                .country(guide.getCountry())
                .city(guide.getCity())
                .closeTimeStart(LocalTime.parse(guide.getTimeStart()))
                .closeTimeEnd(LocalTime.parse(guide.getTimeEnd()))
                .build();
        */
        Guide newGuide = Guide.builder()
                .user(user.get())
                .build();

        guideRepository.save(newGuide);
        

        return 1;
    }


    ////////////////상담 날짜 관리 기능/////////////////////

    @Override
    public int consultDateRegister(String userId, LocalDate dayoff) {
        Optional<User> checkUser = userRepository.findById(userId);//로그인 아이디로 사용자 얻기
        if (!checkUser.isPresent()) return 0;
        System.out.println("user id" + checkUser.get().getUserId());

        int id = checkUser.get().getUserId();
        Optional<Guide> checkGuide = guideRepository.findGuideByUser(id);//위에서 얻은 사용자로 가이드인지 확인함
        if (!checkGuide.isPresent()) return 0;

        //상담 불가능한 날짜를 추가함
        DayOff dayOff = DayOff.builder().dayOffDate(dayoff).guide(checkGuide.get()).build();
        dayOffRepository.save(dayOff);

        return 1;
    }

    //상담 날짜 삭제
    @Override
    public int consultDateDelete(String userId, int dayOffId) {

        //해당 가이드의 상담 날짜가 존재하는지 확인함
        Optional<DayOff> day = dayOffRepository.findDayOffByDayOffId(dayOffId);
        if (!day.isPresent()) return 0;

        dayOffRepository.deleteById(dayOffId);

        return 1;
    }


    ////////////////////상담 시간대 관리 기능/////////////////////////////

    @Override
    public int consultTimeRegister(LocalTime startTime, LocalTime endTime, String userId) {
        //해당 가이드가 존재하는지 확인한다.
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) return 0;

        Optional<Guide> existGuide = guideRepository.findGuideByUser(user.get().getUserId());
        if (!existGuide.isPresent()) return 0;

        Guide guideInfo = existGuide.get();
        System.out.println("start = " + startTime);
        System.out.println("end = " + endTime);
        guideInfo.setCloseTimeStart(startTime);
        guideInfo.setCloseTimeEnd(endTime);

        guideRepository.save(guideInfo);

        return 1;
    }

    @Override
    public int consultTimeDelete(String loginId) {
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return 0;

        Optional<Guide> existGuide = guideRepository.findGuideByUser(user.get().getUserId());
        if (!existGuide.isPresent()) return 0;

        Guide guideInfo = existGuide.get();
        guideRepository.updateTime(guideInfo.getGuideId());

        return 1;
    }



    ////////////////////여행 테마 관리///////////////////////
    @Override
    public int themaRegister(String loginId, String themaName) {
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return 0;

        Optional<Guide> existGuide = guideRepository.findGuideByUser(user.get().getUserId());
        if (!existGuide.isPresent()) return 0;

        String code = getCode(themaName);

        GuideThema thema = GuideThema.builder().themaCode(code).guide(existGuide.get()).build();

        guideThemaRepository.save(thema);

        return 1;
    }

    @Override
    public int themaDelete(String loginId, int themaId) {
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return 0;

        Optional<Guide> existGuide = guideRepository.findGuideByUser(user.get().getUserId());
        if (!existGuide.isPresent()) return 0;

        log.info(existGuide.get().getGuideId());
        log.info(themaId);
        //테마를 삭제함
        int result = guideThemaRepository.delThema(existGuide.get().getGuideId(), themaId);

        return result;
    }

    @Override
    public List<ThemaResponse> themaSelect(String loginId) {
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return null;

        User getUser = user.get();
        Optional<Guide> guide = guideRepository.findGuideByUser(getUser.getUserId());
        if (!guide.isPresent()) return null;

        List<GuideThema> themaList = guideThemaRepository.findByGuide(guide.get());
        List<ThemaResponse> response = new ArrayList<>();

        for (GuideThema thema : themaList) {
            String name = getCodeDescritpion(thema.getThemaCode());
            ThemaResponse res = ThemaResponse.builder()
                    .guideId(guide.get().getGuideId())
                    .themaCode(thema.getThemaCode())
                    .themaName(name)
                    .themaId(thema.getThemaId())
                    .build();
            response.add(res);
        }

        return response;
    }

    /////////////////////////마이페이지 기능////////////////////////////

    @Override
    public GuideResponse getMyPage(String loginId) {
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return null;

        User getUser = user.get();
        Optional<Guide> guide = guideRepository.findGuideByUser(getUser.getUserId());
        if (!guide.isPresent()) return null;

        Guide getGuide = guide.get();

        GuideResponse response = GuideResponse.builder()
                .guideId(getGuide.getGuideId())
                .country(getGuide.getCountry())
                .price(getGuide.getPrice())
                .introduction(getGuide.getIntroduction())
                .onelineIntroduction(getGuide.getOnlineIntroduction())
                .city(getGuide.getCity())
                .picture(getGuide.getPicture())
                .build();

        return response;
    }

    @Override
    public int setMyPage(MyPageRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        if (!user.isPresent()) return 0;

        User getUser = user.get();
        Optional<Guide> guide = guideRepository.findGuideByUser(getUser.getUserId());
        if (!guide.isPresent()) return 0;

        Guide updateGuide = guide.get();//가이드 얻어옴

        if (request.getCity() != null) {
            updateGuide.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            updateGuide.setCountry(request.getCountry());
        }
        if (request.getIntroduction() != null) {
            updateGuide.setIntroduction(request.getIntroduction());

        }
        if (request.getOnelineIntroduction() != null) {
            updateGuide.setOnlineIntroduction(request.getOnelineIntroduction());
        }
        if (request.getPrice() != 0) {
            updateGuide.setPrice(request.getPrice());
        }
        if (request.getPicture() != null) {
            updateGuide.setPicture(request.getPicture());
        }


        guideRepository.save(updateGuide);

        return 1;
    }

    @Override
    public String findGuideProfilePicture(String loginId) {
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return null;

        User getUser = user.get();
        Optional<Guide> guide = guideRepository.findGuideByUser(getUser.getUserId());
        if (!guide.isPresent()) return null;

        Guide getGuide = guide.get();

        String file = getGuide.getPicture();
        return file;
    }


    //////////////////////////////////가이드 언어 관리 기능/////////////////////////////////////////

    @Override
    public int languageRegister(LanguageRequest request) {
        //해당 아이디로 가이드 PK 얻어오기
        Optional<User> user = userRepository.findById(request.getLoginId());
        if (!user.isPresent()) return 0;

        User getUser = user.get();
        Optional<Guide> guide = guideRepository.findGuideByUser(getUser.getUserId());
        if (!guide.isPresent()) return 0;

        Guide getGuide = guide.get();
        String code = getCode(request.getLanguageName());

        GuideLanguage language = GuideLanguage.builder()
                .guide(getGuide)
                .langCode(code)
                .build();

        languageRepository.save(language);

        return 0;
    }

    @Override
    public int languageDelete(String loginId, int langId) {
        //로그인 아이디로 가이드 정보 얻어오기
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return 0;

        User getUser = user.get();
        Optional<Guide> guide = guideRepository.findGuideByUser(getUser.getUserId());
        if (!guide.isPresent()) return 0;

        int result = languageRepository.deleteByLanguageId(langId);

        return result;
    }

    @Override
    public List<LanguageResponse> getLanguage(String loginId) {
        //로그인 아이디로 가이드 정보 얻어오기
        Optional<User> user = userRepository.findById(loginId);
        if (!user.isPresent()) return null;

        User getUser = user.get();
        Optional<Guide> guide = guideRepository.findGuideByUser(getUser.getUserId());
        if (!guide.isPresent()) return null;

        List<GuideLanguage> languageList = languageRepository.findByGuide(guide.get());
        List<LanguageResponse> response = new ArrayList<>();

        for (GuideLanguage lang : languageList) {
            LanguageResponse res = new LanguageResponse();
            String description = getCodeDescritpion(lang.getLangCode());
            res.setLanguage_id(lang.getLanguageId());
            res.setLanguageName(description);

            response.add(res);
        }


        return response;
    }

    //해당 테마의 코드번호 가져오기
    private String getCode(String name) {
        return languageRepository.searchCode(name);
    }

    private String getCodeDescritpion(String code) {
        return languageRepository.searchCodeName(code);
    }

}
