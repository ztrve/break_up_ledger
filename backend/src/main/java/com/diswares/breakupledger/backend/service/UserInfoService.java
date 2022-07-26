package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.po.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author A
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

}
