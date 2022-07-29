package com.diswares.breakupledger.backend.dto;

import com.diswares.breakupledger.backend.po.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * @author: z_true
 * @date: 2022/7/29 16:11
 * @version: 1.0.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class AuthUser extends User {
    private final UserInfo userInfo;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UserInfo userInfo) {
        super(username, password, authorities);
        this.userInfo = userInfo;
    }

    public AuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, UserInfo userInfo) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userInfo = userInfo;
    }

}
