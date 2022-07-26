package com.diswares.breakupledger.backend.qo.wx;

import lombok.Data;

@Data
public class WxUserInfoQo {
    private String avatarUrl;
    private String city;
    private String country;
    private String gender;
    private String language;
    private String nickName;
    private String province;
}
