package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.mapper.UserInfoMapper;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
* @author z_true
* @description 针对表【user_info(用户信息)】的数据库操作Service实现
* @createDate 2022-07-26 23:14:26
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Override
    public UserInfo getByWxOpenId(String wxOpenId) {
        LambdaQueryWrapper<UserInfo> query = new LambdaQueryWrapper<>();
        query.eq(UserInfo::getWxOpenId, wxOpenId)
                .last("limit 1");
        return getOne(query);
    }

    @Override
    public UserInfo getByUserCharacteristics(String userCharacteristics) {
        LambdaQueryWrapper<UserInfo> query = new LambdaQueryWrapper<>();
        query.eq(UserInfo::getPhone, userCharacteristics)
                .or()
                .eq(UserInfo::getCode, userCharacteristics)
                .last("limit 1");
        return getOne(query);
    }

    @Override
    public boolean isRealUsers(List<Long> userIds) {
        if (ObjectUtils.isEmpty(userIds)) {
            return true;
        }
        LambdaQueryWrapper<UserInfo> userInfoQuery = new LambdaQueryWrapper<>();
        userInfoQuery.in(UserInfo::getId, userIds);
        int userCount = count(userInfoQuery);
        return userIds.size() == userCount;
    }
}




