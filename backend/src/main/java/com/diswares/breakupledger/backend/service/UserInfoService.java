package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.po.user.UserInfo;

import java.util.List;

/**
* @author z_true
* @description 针对表【user_info(用户信息)】的数据库操作Service
* @createDate 2022-07-26 23:14:26
*/
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 通过 OpenId 查询用户信息
     *
     * @param wxOpenId wxOpenId
     * @return 平台用户信息
     */
    UserInfo getByWxOpenId (String wxOpenId);

    /**
     * 根据用户特征查询一个用户
     *
     * @param userCharacteristics 用户特征
     * @return 用户信息
     */
    UserInfo getByUserCharacteristics(String userCharacteristics);

    /**
     * 是真实的用户
     *
     * @param userIds user id list
     * @return true 全是 false 有的不是
     */
    boolean isRealUsers(List<Long> userIds);
}
