package com.diswares.breakupledger.backend.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.mapper.UserInfoMapper;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<UserInfoVo> listVoByUserIds(List<Long> userIds) {
        List<UserInfo> userInfos = listByUserIds(userIds);
        if (ObjectUtils.isEmpty(userInfos)) {
            return null;
        }
        return userInfos
                .stream()
                .map(userInfo -> {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(userInfo, userInfoVo);
                    return userInfoVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInfo> listByUserIds(List<Long> userIds) {
        if (ObjectUtils.isEmpty(userIds)) {
            return null;
        }
        LambdaQueryWrapper<UserInfo> userInfoQuery = new LambdaQueryWrapper<>();
        userInfoQuery.in(UserInfo::getId, userIds);
        return list(userInfoQuery);
    }
}




