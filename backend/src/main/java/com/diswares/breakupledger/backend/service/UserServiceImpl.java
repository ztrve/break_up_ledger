package com.diswares.breakupledger.backend.service;

import com.alibaba.fastjson.JSON;
import com.diswares.breakupledger.backend.config.configurationproperties.WxConfigurationProperties;
import com.diswares.breakupledger.backend.dto.AuthUser;
import com.diswares.breakupledger.backend.enums.ReqPlatformEnums;
import com.diswares.breakupledger.backend.exception.WxAuthException;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.user.UserLoginQo;
import com.diswares.breakupledger.backend.qo.user.UserRegisterQo;
import com.diswares.breakupledger.backend.remote.WxRemote;
import com.diswares.breakupledger.backend.util.JwtTokenUtil;
import com.diswares.breakupledger.backend.util.SnowFlake;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import com.diswares.breakupledger.backend.vo.user.UserLoginVo;
import com.diswares.breakupledger.backend.vo.user.UserRegisterVo;
import com.diswares.breakupledger.backend.vo.wx.WxJsCode2SessionVo;
import jodd.bean.BeanCopy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

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
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final WxRemote wxRemote;
    private final WxConfigurationProperties wxConfigurationProperties;

    @Override
    public UserRegisterVo register(UserRegisterQo userRegisterQo) {
        if (!ReqPlatformEnums.WX.equals(userRegisterQo.getReqPlatform())) {
            throw new WxAuthException("不支持的平台");
        }

        WxJsCode2SessionVo wxJsCode2SessionVo = wxJsCode2OpenId(userRegisterQo.getData().getJsCode());

        UserInfo userInfo = userInfoService.getByWxOpenId(wxJsCode2SessionVo.getOpenId());
        String nickName = userRegisterQo.getData().getUserProfile().getUserInfo().getNickName();
        String avatarUrl = userRegisterQo.getData().getUserProfile().getUserInfo().getAvatarUrl();
        if (!ObjectUtils.isEmpty(userInfo)) {
            throw new RuntimeException("注册失败, 用户已存在");
        }

        userInfo = new UserInfo();
        userInfo.setCode(SnowFlake.nextId() + "");
        userInfo.setNickname(nickName);
        userInfo.setAvatarUrl(avatarUrl);
        userInfo.setPhone(null);
        userInfo.setWxOpenId(wxJsCode2SessionVo.getOpenId());
        userInfoService.save(userInfo);

        UserInfoVo userInfoVo = new UserInfoVo();
        BeanCopy.beans(userInfo, userInfoVo).copy();
        Map<String, Object> userInfoMap = null;
        try {
            //noinspection unchecked
            userInfoMap = BeanUtils.describe(userInfo);
        } catch (Exception ignored) {}
        final String token = jwtTokenUtil.generateToken(userInfoMap, userInfo.getWxOpenId());
        UserRegisterVo userRegisterVo = new UserRegisterVo();
        userRegisterVo.setUser(userInfoVo);
        userRegisterVo.setToken(token);
        return userRegisterVo;
    }

    @Override
    public UserLoginVo login(UserLoginQo loginQo) {
        if (!ReqPlatformEnums.WX.equals(loginQo.getReqPlatform())) {
            throw new WxAuthException("不支持的平台");
        }
        WxJsCode2SessionVo wxJsCode2SessionVo = wxJsCode2OpenId(loginQo.getData().getJsCode());

        Objects.requireNonNull(wxJsCode2SessionVo.getOpenId());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(wxJsCode2SessionVo.getOpenId(), wxJsCode2SessionVo.getOpenId()));

        final AuthUser authUser = (AuthUser) userDetailsService.loadUserByUsername(wxJsCode2SessionVo.getOpenId());

        Map<String, Object> userInfoMap = null;
        try {
            //noinspection unchecked
            userInfoMap = BeanUtils.describe(authUser.getUserInfo());
        } catch (Exception ignored) {}
        final String token = jwtTokenUtil.generateToken(userInfoMap, authUser.getUserInfo().getWxOpenId());

        UserInfo userInfo = userInfoService.getByWxOpenId(wxJsCode2SessionVo.getOpenId());
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanCopy.beans(userInfo, userInfoVo).copy();

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setToken(token);
        userLoginVo.setUser(userInfoVo);
        return userLoginVo;
    }

    /**
     * 微信 JS Code 转 openId
     *
     * @param wxJsCode wxJsCode
     * @return 带 Wx User OpenId 的返回值
     */
    @NotNull
    private WxJsCode2SessionVo wxJsCode2OpenId(String wxJsCode) {
        String jsCode2SessionResultStr = wxRemote.jsCode2Session(wxConfigurationProperties.getAppId(), wxConfigurationProperties.getAppSecret(),
                wxJsCode, "authorization_code");
        WxJsCode2SessionVo wxJsCode2SessionVo = JSON.parseObject(jsCode2SessionResultStr, WxJsCode2SessionVo.class);
        if (ObjectUtils.isEmpty(wxJsCode2SessionVo)) {
            throw new WxAuthException("微信认证失败!");
        }
        return wxJsCode2SessionVo;
    }

}
