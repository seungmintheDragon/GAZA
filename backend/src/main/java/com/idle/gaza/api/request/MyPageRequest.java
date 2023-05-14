package com.idle.gaza.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ApiModel("MyPageRequest")
@ToString
public class MyPageRequest {

    private String userId;

    private String onelineIntroduction;

    private String introduction;

    private int price;

    private String country;

    private String city;

    private String picture;

}
