package com.diswares.breakupledger.backend.qo.user;

import com.diswares.breakupledger.backend.enums.ReqPlatformEnums;
import com.diswares.breakupledger.backend.qo.wx.WxLoginDataQo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登陆 Qo
 *
 * @author: z_true
 * @date: 2022/7/26 14:33
 * @version: 1.0.0
 */
@Data
public class UserUpdatePhoneQo {
    @NotBlank(message = "手机号不得为空")
    @Pattern(message = "手机号不正确", regexp = "^(?:(?:\\+|00)86)?1\\d{10}$")
    private String phone;
}
