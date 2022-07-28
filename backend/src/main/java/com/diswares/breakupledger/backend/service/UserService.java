package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.user.LoginQo;
import com.diswares.breakupledger.backend.vo.user.LoginVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author: z_true
 * @date: 2022/7/26 14:42
 * @version: 1.0.0
 */
public interface UserService {

    /**
     * 登陆
     *
     * @param loginQo 登陆 qo
     * @param wxUserOpenId 微信用户 openId
     * @return 登陆信息
     */
    UserInfoVo login(LoginQo loginQo, String wxUserOpenId);

}
