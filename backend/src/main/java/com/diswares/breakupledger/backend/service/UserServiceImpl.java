package com.diswares.breakupledger.backend.service;

import com.alibaba.fastjson.JSON;
import com.diswares.breakupledger.backend.config.configurationproperties.AuthorLoginConfigurationProperties;
import com.diswares.breakupledger.backend.config.configurationproperties.WxConfigurationProperties;
import com.diswares.breakupledger.backend.helper.rediskey.AuthorKeyConfig;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.user.LoginQo;
import com.diswares.breakupledger.backend.remote.WxRemote;
import com.diswares.breakupledger.backend.vo.user.LoginVo;
import com.diswares.breakupledger.backend.vo.wx.WxJsCode2SessionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    private final WxRemote wxRemote;

    private final UserInfoService userInfoService;

    private final WxConfigurationProperties wxConfigurationProperties;

    private final AuthorKeyConfig authorKeyConfig;

    private final StringRedisTemplate stringRedisTemplate;

    private final AuthorLoginConfigurationProperties authorLoginConfigurationProperties;

    @Override
    public LoginVo login(LoginQo loginQo) {
        try {
            String jsCode2SessionResultStr = wxRemote.jsCode2Session(wxConfigurationProperties.getAppId(), wxConfigurationProperties.getAppSecret(),
                    loginQo.getData().getJsCode(), "authorization_code");
            WxJsCode2SessionVo wxJsCode2SessionVo = JSON.parseObject(jsCode2SessionResultStr, WxJsCode2SessionVo.class);
            log.info("wx login result = {}", wxJsCode2SessionVo);

            UserInfo userInfo = userInfoService.getByWxOpenId(wxJsCode2SessionVo.getOpenId());

            if (ObjectUtils.isEmpty(userInfo)) {
                userInfo = new UserInfo();
                userInfo.setNickname(loginQo.getData().getUserProfile().getUserInfo().getNickName());
                userInfo.setAvatarUrl(loginQo.getData().getUserProfile().getUserInfo().getAvatarUrl());
                userInfo.setPhone(null);
                userInfo.setWxOpenId(wxJsCode2SessionVo.getOpenId());
                userInfoService.save(userInfo);
            }
            // TODO JWT生成TOKEN
            // 将openId改为,token
//            String loginKey = authorKeyConfig.initLoginKey(wxJsCode2SessionVo.getOpenId());
//            stringRedisTemplate.opsForValue().set(loginKey, "", authorLoginConfigurationProperties.getExpireSecond(), TimeUnit.SECONDS);

            LoginVo loginVo = new LoginVo();
            loginVo.setUser(userInfo);
            // TODO JWT生成TOKEN
            loginVo.setToken("token");
            return loginVo;
        } catch (Exception e) {
            log.error("", e);
        }
        // TODO 抛出异常
        return null;
    }
}
