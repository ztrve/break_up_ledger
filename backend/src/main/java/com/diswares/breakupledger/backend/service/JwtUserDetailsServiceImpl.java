package com.diswares.breakupledger.backend.service;

import com.alibaba.fastjson.JSON;
import com.diswares.breakupledger.backend.config.configurationproperties.WxConfigurationProperties;
import com.diswares.breakupledger.backend.remote.WxRemote;
import com.diswares.breakupledger.backend.vo.wx.WxJsCode2SessionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;

import static com.diswares.breakupledger.backend.config.auth.WebSecurityConfig.PWD_ENCODER;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String wxUserOpenId) throws UsernameNotFoundException {
        if (ObjectUtils.isEmpty(wxUserOpenId)) {
            throw new UsernameNotFoundException("WX check fault!");
        } else {
            return new User(wxUserOpenId, PWD_ENCODER.encode(wxUserOpenId), new ArrayList<>());
        }
    }
}
