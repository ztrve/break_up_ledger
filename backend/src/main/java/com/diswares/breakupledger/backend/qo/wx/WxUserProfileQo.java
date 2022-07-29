package com.diswares.breakupledger.backend.qo.wx;

import lombok.Data;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@Data
public class WxUserProfileQo {
    private String cloudID;
    private String encryptedData;
    private String iv;
    private String signature;
    private WxUserInfoQo userInfo;
}
