package com.diswares.breakupledger.backend.qo.wx;

import lombok.Data;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
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
