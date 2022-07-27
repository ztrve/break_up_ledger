package com.diswares.breakupledger.backend.controller;

import com.alibaba.fastjson.JSON;
import com.diswares.breakupledger.backend.config.configurationproperties.WxConfigurationProperties;
import com.diswares.breakupledger.backend.qo.user.LoginQo;
import com.diswares.breakupledger.backend.remote.WxRemote;
import com.diswares.breakupledger.backend.service.UserService;
import com.diswares.breakupledger.backend.util.JwtTokenUtil;
import com.diswares.breakupledger.backend.vo.user.LoginVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import com.diswares.breakupledger.backend.vo.wx.WxJsCode2SessionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    private final WxRemote wxRemote;

    private final WxConfigurationProperties wxConfigurationProperties;


    @PostMapping("/login")
    public LoginVo createAuthenticationToken(@RequestBody LoginQo loginQo)
            throws Exception {
        String jsCode2SessionResultStr = wxRemote.jsCode2Session(wxConfigurationProperties.getAppId(), wxConfigurationProperties.getAppSecret(),
                loginQo.getData().getJsCode(), "authorization_code");
        WxJsCode2SessionVo wxJsCode2SessionVo = JSON.parseObject(jsCode2SessionResultStr, WxJsCode2SessionVo.class);

        authenticate(wxJsCode2SessionVo.getOpenId());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(wxJsCode2SessionVo.getOpenId());
        final String token = jwtTokenUtil.generateToken(userDetails);

        UserInfoVo userInfoVo = userService.login(loginQo, userDetails.getUsername());

        LoginVo loginVo = new LoginVo();
//        loginVo.setUser(userInfoVo);
        loginVo.setToken(token);
        return loginVo;
    }

    private void authenticate(String wxOpenId) throws Exception {
        Objects.requireNonNull(wxOpenId);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(wxOpenId, wxOpenId));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
