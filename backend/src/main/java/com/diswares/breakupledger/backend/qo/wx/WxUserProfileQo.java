package com.diswares.breakupledger.backend.qo.wx;

import lombok.Data;

@Data
public class WxUserProfileQo {
    private String cloudID;
    private String encryptedData;
    private String iv;
    private String signature;
    private WxUserInfoQo userInfo;
}
