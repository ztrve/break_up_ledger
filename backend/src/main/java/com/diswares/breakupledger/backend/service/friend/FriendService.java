package com.diswares.breakupledger.backend.service.friend;

import com.diswares.breakupledger.backend.po.friend.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;

import java.util.List;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
public interface FriendService extends IService<Friend> {
    /**
     * 我的朋友
     *
     * @return 朋友列表
     */
    List<UserInfoVo> myFriends ();

    /**
     * 用户2 是 用户1 的朋友
     *
     * @param uid1 用户1
     * @param uid2 用户2
     * @return true 是朋友 false 不是朋友
     */
    boolean u2IsU1Friend(Long uid1, Long uid2);

    /**
     * 是我的朋友
     *
     * @param userIds 待校验用户
     * @return true都是我的朋友 false有一个或多个不是我的朋友
     */
    boolean isFriends(Long u1, List<Long> userIds);

}
