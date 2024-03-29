package com.diswares.breakupledger.backend.service.user;

import com.diswares.breakupledger.backend.qo.user.UserLoginQo;
import com.diswares.breakupledger.backend.qo.user.UserRegisterQo;
import com.diswares.breakupledger.backend.qo.user.UserUpdatePhoneQo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import com.diswares.breakupledger.backend.vo.user.UserLoginVo;
import com.diswares.breakupledger.backend.vo.user.UserRegisterVo;

import java.lang.reflect.InvocationTargetException;

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

    /**
     * 更新手机号
     *
     * @param userUpdatePhoneQo qo
     * @return 用户信息
     */
    void updatePhone(UserUpdatePhoneQo userUpdatePhoneQo);
}
