package com.idle.gaza.api.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel("GuideResponse")
@Getter
@Setter
@NoArgsConstructor
public class GuideResponse {

    private int guideId;

    private int userId;

    private String name;

    private String picture;

    private String country;

    private String city;

    private LocalTime closeTimeStart;

    private LocalTime closeTimeEnd;

    private Integer price;

    private Integer license;

    private String onelineIntroduction;

    private String introduction;

    private String gender;

    private List<String> language = new ArrayList<>();

    private List<String> thema = new ArrayList<>();

    private List<ReservationResponse> reservationList = new ArrayList<>();

    private List<ReviewResponse> reviewList = new ArrayList<>();

    private List<DayOffResponse> dayOffList = new ArrayList<>();

    private List<LocationResponse> guideLocationList = new ArrayList<>();

    private String id;


    @Builder
    public GuideResponse(int guideId, int userId, String name, String picture, String country, String city, LocalTime closeTimeStart, LocalTime closeTimeEnd, Integer price, Integer license, String onelineIntroduction, String introduction, String gender, List<String> language, List<String> thema, List<ReservationResponse> reservationList, List<ReviewResponse> reviewList, List<DayOffResponse> dayOffList, List<LocationResponse> guideLocationList, String id) {
        this.guideId = guideId;
        this.userId = userId;
        this.name = name;
        this.picture = picture;
        this.country = country;
        this.city = city;
        this.closeTimeStart = closeTimeStart;
        this.closeTimeEnd = closeTimeEnd;
        this.price = price;
        this.license = license;
        this.onelineIntroduction = onelineIntroduction;
        this.introduction = introduction;
        this.gender = gender;
        this.language = language;
        this.thema = thema;
        this.reservationList = reservationList;
        this.reviewList = reviewList;
        this.dayOffList = dayOffList;
        this.guideLocationList = guideLocationList;
        this.id = id;
    }

}
