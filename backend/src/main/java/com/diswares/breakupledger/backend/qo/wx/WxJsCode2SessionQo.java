package com.diswares.breakupledger.backend.qo.wx;

import lombok.Data;

/**
 * @author: z_true
 * @date: 2022/7/26 15:07
 * @version: 1.0.0
 */
@Data
public class WxJsCode2SessionQo {
    private String appId;
    private String secret;
    private String jsCode;
    private String grantType;
}
