package com.cooperative.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = -7666935963856057437L;

    private String avatarUrl;

    private String city;

    private String country;

    private Integer gender;

    private String nickName;

    private String province;
}
