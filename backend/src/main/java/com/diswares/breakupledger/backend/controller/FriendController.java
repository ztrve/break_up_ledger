package com.diswares.breakupledger.backend.controller;

import com.diswares.breakupledger.backend.service.FriendService;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: z_true
 * @date: 2022/7/29 14:52
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/my")
    public List<UserInfoVo> myFriends() {
        return friendService.myFriends();
    }
}
