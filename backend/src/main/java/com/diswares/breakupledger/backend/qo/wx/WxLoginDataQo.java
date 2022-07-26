package com.diswares.breakupledger.backend.qo.wx;

import lombok.Data;

@Data
public class WxLoginDataQo {
    private String jsCode;
    private WxUserProfileQo userProfile;
}
