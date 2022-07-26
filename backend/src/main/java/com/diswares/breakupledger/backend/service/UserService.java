package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.user.LoginQo;
import com.diswares.breakupledger.backend.vo.user.LoginVo;

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
     * @return 登陆信息
     */
    LoginVo login(LoginQo loginQo);

}
