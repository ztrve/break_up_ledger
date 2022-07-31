package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.dto.AuthUser;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

import static com.diswares.breakupledger.backend.config.auth.WebSecurityConfig.PWD_ENCODER;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    private final UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String wxUserOpenId) throws UsernameNotFoundException {
        if (ObjectUtils.isEmpty(wxUserOpenId)) {
            throw new UsernameNotFoundException("WX check fault!");
        }
        UserInfo userInfo = userInfoService.getByWxOpenId(wxUserOpenId);
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new UsernameNotFoundException("No user!");
        }
        return new AuthUser(wxUserOpenId, PWD_ENCODER.encode(wxUserOpenId), new ArrayList<>(), userInfo);
    }
}
