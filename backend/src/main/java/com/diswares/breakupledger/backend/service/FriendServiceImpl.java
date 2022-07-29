package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.Friend;
import com.diswares.breakupledger.backend.mapper.FriendMapper;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
        implements FriendService {

    private final UserInfoService userInfoService;


    @Override
    public List<UserInfoVo> myFriends() {
        UserInfo me = AuthUtil.currentUserInfo();

        LambdaQueryWrapper<Friend> friendQuery = new LambdaQueryWrapper<>();
        friendQuery.eq(Friend::getLeftUserId, me.getId());
        List<Friend> friends = list(friendQuery);
        if (ObjectUtils.isEmpty(friends)) {
            return null;
        }
        List<Long> friendIdList = friends.stream().map(Friend::getRightUserId).collect(Collectors.toList());
        LambdaQueryWrapper<UserInfo> userQuery = new LambdaQueryWrapper<>();
        userQuery.in(UserInfo::getId, friendIdList);

        List<UserInfo> userInfos = userInfoService.list(userQuery);
        return userInfos.stream().map(userInfo -> {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(userInfo, userInfoVo);
                    return userInfoVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean u2IsU1Friend(Long uid1, Long uid2) {
        LambdaQueryWrapper<Friend> query = new LambdaQueryWrapper<>();
        query.eq(Friend::getLeftUserId, uid1);
        query.eq(Friend::getRightUserId, uid2);
        return count(query) >= 0;
    }
}




