package com.diswares.breakupledger.backend.controller;

import com.diswares.breakupledger.backend.enums.ReqPlatformEnums;
import com.diswares.breakupledger.backend.qo.user.LoginQo;
import com.diswares.breakupledger.backend.service.UserService;
import com.diswares.breakupledger.backend.vo.user.LoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public LoginVo login (@RequestBody LoginQo loginQo) {
        return userService.login(loginQo);
    }
}
