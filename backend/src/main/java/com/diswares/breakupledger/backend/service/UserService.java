package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.qo.user.UserLoginQo;
import com.diswares.breakupledger.backend.qo.user.UserRegisterQo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import com.diswares.breakupledger.backend.vo.user.UserLoginVo;
import com.diswares.breakupledger.backend.vo.user.UserRegisterVo;

/**
 * @author: z_true
 * @date: 2022/7/26 14:42
 * @version: 1.0.0
 */
public interface UserService {

    /**
     * 注册
     *
     * @param userRegisterQo 注册 qo
     * @return 用户信息
     */
    UserRegisterVo register(UserRegisterQo userRegisterQo);

    /**
     * 登录
     *
     * @param loginQo 登录 qo
     * @return 用户信息
     */
    UserLoginVo login(UserLoginQo loginQo);
}
