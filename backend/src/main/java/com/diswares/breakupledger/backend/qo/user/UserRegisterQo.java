package com.diswares.breakupledger.backend.qo.user;

import com.diswares.breakupledger.backend.enums.ReqPlatformEnums;
import com.diswares.breakupledger.backend.qo.wx.WxRegisterDataQo;
import lombok.Data;

/**
 * 登陆 Qo
 *
 * @author: z_true
 * @date: 2022/7/26 14:33
 * @version: 1.0.0
 */
@Data
public class UserRegisterQo {
    /**
     * 数据
     */
    private WxRegisterDataQo data;

    /**
     * 登陆的平台
     */
    private ReqPlatformEnums reqPlatform;
}
