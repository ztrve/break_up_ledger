package com.diswares.breakupledger.backend.service;

import com.alibaba.fastjson.JSON;
import com.diswares.breakupledger.backend.config.configurationproperties.AuthorLoginConfigurationProperties;
import com.diswares.breakupledger.backend.config.configurationproperties.WxConfigurationProperties;
import com.diswares.breakupledger.backend.helper.rediskey.AuthorKeyConfig;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.user.LoginQo;
import com.diswares.breakupledger.backend.remote.WxRemote;
import com.diswares.breakupledger.backend.util.SnowFlake;
import com.diswares.breakupledger.backend.vo.user.LoginVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import com.diswares.breakupledger.backend.vo.wx.WxJsCode2SessionVo;
import jodd.bean.BeanCopy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author: z_true
 * @date: 2022/7/26 14:42
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserInfoService userInfoService;

    @Override
    public UserInfoVo login(LoginQo loginQo, String wxUserOpenId) {
        try {
            UserInfo userInfo = userInfoService.getByWxOpenId(wxUserOpenId);
            String nickName = loginQo.getData().getUserProfile().getUserInfo().getNickName();
            String avatarUrl = loginQo.getData().getUserProfile().getUserInfo().getAvatarUrl();
            if (ObjectUtils.isEmpty(userInfo)) {
                userInfo = new UserInfo();
                userInfo.setCode(SnowFlake.nextId() + "");
                userInfo.setNickname(nickName);
                userInfo.setAvatarUrl(avatarUrl);
                userInfo.setPhone(null);
                userInfo.setWxOpenId(wxUserOpenId);
                userInfoService.save(userInfo);
            } else {
                boolean userInfoChanged = false;
                if (!userInfo.getNickname().equals(nickName)) {
                    userInfo.setNickname(nickName);
                    userInfoChanged = true;
                }
                if (!userInfo.getAvatarUrl().equals(avatarUrl)) {
                    userInfo.setAvatarUrl(avatarUrl);
                    userInfoChanged = true;
                }
                if (userInfoChanged) {
                    userInfoService.updateById(userInfo);
                }
            }
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanCopy.beans(userInfo, userInfoVo).copy();
            return userInfoVo;
        } catch (Exception e) {
            log.error("", e);
        }
        // TODO 抛出异常
        return null;
    }
}
