package com.idle.gaza.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Guide{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="guide_id")
    private Integer guideId;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="user_id")
    private User user;


    @Column(name="oneline_introduction")
    private String onlineIntroduction;

    private String introduction;

    private String picture;

    private String country;

    private String city;

    @Column(name="close_time_start")
    private LocalTime closeTimeStart;

    @Column(name="close_time_end")
    private LocalTime closeTimeEnd;

    private Integer price;

    @Column(columnDefinition = "TINYINT", length=1)
    private Integer license;

    @OneToMany(mappedBy = "guide")
    private List<GuideRecommendLocation> guideLocationList = new ArrayList<>();

    @OneToMany(mappedBy = "guide")
    private List<DayOff> dayOffList = new ArrayList<>();

    @OneToMany(mappedBy = "guide")
    private List<GuideThema> guideThemaList = new ArrayList<>();

    @OneToMany(mappedBy = "guide")
    private List<GuideLanguage> languageList = new ArrayList<>();

    @OneToMany(mappedBy = "guideId")
    private List<Reservation> reservationList = new ArrayList<>();

    @Builder
    public Guide(Integer guideId, User user, String onlineIntroduction, String introduction, String picture, String country, String city, LocalTime closeTimeStart, LocalTime closeTimeEnd, Integer price, Integer license, List<GuideRecommendLocation> guideLocationList, List<DayOff> dayOffList, List<GuideThema> guideThemaList, List<GuideLanguage> languageList, List<Reservation> reservationList) {
        this.guideId = guideId;
        this.user = user;
        this.onlineIntroduction = onlineIntroduction;
        this.introduction = introduction;
        this.picture = picture;
        this.country = country;
        this.city = city;
        this.closeTimeStart = closeTimeStart;
        this.closeTimeEnd = closeTimeEnd;
        this.price = price;
        this.license = license;
        this.guideLocationList = guideLocationList;
        this.dayOffList = dayOffList;
        this.guideThemaList = guideThemaList;
        this.languageList = languageList;
        this.reservationList = reservationList;
    }




    public void addGuideLocation(GuideRecommendLocation guideLocationList){
        this.guideLocationList.add(guideLocationList);
    }

    public void addDayOffList(DayOff dayOff){
        this.dayOffList.add(dayOff);
    }

    public void addGuideThema(GuideThema guideThema){ this.guideThemaList.add(guideThema);}

    @Override
    public String toString() {
        return "Guide{" +
                "guideId=" + guideId +
                ", user=" + user +
                ", onlineIntroduction='" + onlineIntroduction + '\'' +
                ", introduction='" + introduction + '\'' +
                ", picture='" + picture + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", closeTimeStart=" + closeTimeStart +
                ", closeTimeEnd=" + closeTimeEnd +
                ", price=" + price +
                ", license=" + license +
                ", guideLocationList=" + guideLocationList +
                ", dayOffList=" + dayOffList +
                '}';
    }
}
