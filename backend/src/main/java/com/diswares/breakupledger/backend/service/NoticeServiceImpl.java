package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.enums.NoticeDealEnums;
import com.diswares.breakupledger.backend.enums.NoticeEnums;
import com.diswares.breakupledger.backend.po.Notice;
import com.diswares.breakupledger.backend.mapper.NoticeMapper;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.notice.NoticeCreateFriendQo;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.util.StringReplacer;
import com.diswares.breakupledger.backend.vo.notice.NoticeVo;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

    private final UserInfoService userInfoService;

    private final FriendService friendService;

    @Override
    public NoticeVo createNewFriendNotice(NoticeCreateFriendQo noticeCreateNewFriendQo) {
        String searchVal = noticeCreateNewFriendQo.getUserCharacteristics();
        // 通过手机/用户编号匹配用户
        UserInfo maybeFriendUser = userInfoService.getByUserCharacteristics(searchVal);
        Assert.notNull(maybeFriendUser, "无该用户，发起邀请");


        // 是否已经是好友关系
        UserInfo myInfo = AuthUtil.currentUserInfo();
        Assert.isTrue(!searchVal.equals(myInfo.getPhone()) && !searchVal.equals(myInfo.getCode()), "不能加自己啊，笨逼");

        boolean u2IsU1Friend = friendService.u2IsU1Friend(myInfo.getId(), maybeFriendUser.getId());
        Assert.isTrue(!u2IsU1Friend, "Ta已经是你的好友");

        NoticeEnums friendRequest = NoticeEnums.FRIEND_REQUEST;
        // 创建通知
        Notice notice = new Notice();
        notice.setNoticeType(friendRequest.getType());
        notice.setNoticeName(friendRequest.getName());
        notice.setNoticeMsg(StringReplacer.build(friendRequest.getStrTmpl(), myInfo.getNickname()));
        notice.setNoticeData("");
        notice.setInitiatorId(myInfo.getId());
        notice.setHandlerId(maybeFriendUser.getId());

        // 避免重复请求
        LambdaQueryWrapper<Notice> noticeQuery = new LambdaQueryWrapper<>();
        noticeQuery.eq(Notice::getNoticeType, notice.getNoticeType());
        noticeQuery.eq(Notice::getInitiatorId, notice.getInitiatorId());
        noticeQuery.eq(Notice::getHandlerId, notice.getHandlerId());
        noticeQuery.eq(Notice::getDealStatus, NoticeDealEnums.UN_DEAL);
        noticeQuery.last("limit 1");
        Notice one = getOne(noticeQuery);
        if (ObjectUtils.isEmpty(one)) {
            save(notice);
            notice = getById(notice.getId());
        } else {
            notice = one;
        }

        NoticeVo noticeVo = new NoticeVo();
        BeanUtils.copyProperties(notice, noticeVo);
        return noticeVo;
    }
}




