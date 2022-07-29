package com.diswares.breakupledger.backend.util;

import com.diswares.breakupledger.backend.dto.AuthUser;
import com.diswares.breakupledger.backend.po.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author: z_true
 * @date: 2022/7/29 16:29
 * @version: 1.0.0
 */
public class AuthUtil {

    /**
     * 获取当前web线程中的用户信息
     *
     * 用户信息被存储在 JWT Token 中。由 Spring Security Filter 解析放置入 SecurityContextHolder.getContext()
     * 反向拿出来就行
     *
     * @return 用户信息
     */
    public static UserInfo currentUserInfo () {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return authUser.getUserInfo();
    }
}
