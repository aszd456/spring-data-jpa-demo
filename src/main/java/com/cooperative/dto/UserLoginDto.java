package com.cooperative.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {
    private static final long serialVersionUID = -9006431279481888772L;

    private UserInfoDto userInfo;

    private String code;

    private String v;
}
