package com.diswares.breakupledger.backend.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.enums.NoticeDealEnums;
import com.diswares.breakupledger.backend.enums.NoticeDealResultEnums;
import com.diswares.breakupledger.backend.enums.NoticeEnums;
import com.diswares.breakupledger.backend.helper.notice.NoticeHandler;
import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.diswares.breakupledger.backend.po.notice.Notice;
import com.diswares.breakupledger.backend.mapper.NoticeMapper;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.qo.notice.NoticeCreateFriendQo;
import com.diswares.breakupledger.backend.qo.notice.NoticeDealQo;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.util.StringReplacer;
import com.diswares.breakupledger.backend.vo.notice.NoticeVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
        implements NoticeService {

    private final UserInfoService userInfoService;

    private final FriendService friendService;

    private Map<NoticeEnums, NoticeHandler> noticeHandlerMap = null;

    private final ApplicationContext applicationContext;

    @PostConstruct
    public void post () {
        noticeHandlerMap = applicationContext.getBeansOfType(NoticeHandler.class)
                .values()
                .stream()
                .collect(Collectors.toMap(NoticeHandler::noticeType, v -> v, (k1, k2) -> k1));

    }

    @Override
    public Page<NoticeVo> pageOfMine(Page<Notice> page) {
        UserInfo me = AuthUtil.currentUserInfo();
        LambdaQueryWrapper<Notice> noticeQuery = new LambdaQueryWrapper<>();
        noticeQuery.eq(Notice::getHandlerId, me.getId())
                .orderByDesc(Notice::getCreateTime);
        page = page(page, noticeQuery);

        Page<NoticeVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage, "records");
        if (ObjectUtils.isEmpty(page.getRecords())) {
            return voPage;
        }
        voPage.setRecords(page.getRecords().stream().map(notice -> {
            NoticeVo noticeVo = new NoticeVo();
            BeanUtils.copyProperties(notice, noticeVo);
            return noticeVo;
        }).collect(Collectors.toList()));

        List<Long> initiatorIds = voPage.getRecords().stream().map(NoticeVo::getInitiatorId).collect(Collectors.toList());
        LambdaQueryWrapper<UserInfo> userInfoQuery = new LambdaQueryWrapper<>();
        userInfoQuery.in(UserInfo::getId, initiatorIds);
        Map<Long, UserInfoVo> userIdMap = userInfoService.list(userInfoQuery)
                .stream()
                .map(userInfo -> {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(userInfo, userInfoVo);
                    return userInfoVo;
                })
                .collect(Collectors.toMap(UserInfoVo::getId, v -> v, (k1, k2) -> k1));

        voPage.getRecords()
                .forEach(vo -> {
                    UserInfoVo userInfoVo = userIdMap.get(vo.getInitiatorId());
                    vo.setInitiator(userInfoVo);
                });
        return voPage;
    }

    @Override
    public NoticeVo createNewFriendNotice(NoticeCreateFriendQo noticeCreateNewFriendQo) {
        String searchVal = noticeCreateNewFriendQo.getUserCharacteristics();
        // 通过手机/用户编号匹配用户
        UserInfo maybeFriendUser = userInfoService.getByUserCharacteristics(searchVal);
        Assert.notNull(maybeFriendUser, "查询不到该用户");


        // 是否已经是好友关系
        UserInfo myInfo = AuthUtil.currentUserInfo();
        Assert.isTrue(!searchVal.equals(myInfo.getPhone()) && !searchVal.equals(myInfo.getCode()), "不能加自己啊，笨逼");

        boolean u2IsU1Friend = friendService.u2IsU1Friend(myInfo.getId(), maybeFriendUser.getId());
        Assert.isTrue(!u2IsU1Friend, "Ta已经是你的好友");

        NoticeEnums friendRequest = NoticeEnums.FRIEND_REQUEST;
        // 创建通知
        Notice notice = new Notice();
        notice.setNoticeType(friendRequest);
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

    @Override
    public NoticeVo handleNoticeByType(NoticeDealQo noticeDealQo) {
        Notice notice = getById(noticeDealQo.getNoticeId());
        Assert.notNull(notice, "通知不存在");
        UserInfo me = AuthUtil.currentUserInfo();
        Assert.isTrue(notice.getHandlerId().equals(me.getId()), "无法处理不属于你的通知");

        notice.setDealResult(noticeDealQo.getDealResult());
        notice.setDealStatus(NoticeDealEnums.DEAL);
        updateById(notice);

        NoticeHandler noticeHandler = noticeHandlerMap.get(notice.getNoticeType());

        if (NoticeDealResultEnums.AGREE.equals(noticeDealQo.getDealResult())) {
            noticeHandler.agreeCall(notice);
        } else {
            noticeHandler.disagreeCall(notice);
        }
        NoticeVo noticeVo = new NoticeVo();
        BeanUtils.copyProperties(notice, noticeVo);
        return noticeVo;
    }

    @Override
    public void createLedgerInviteNotice(Ledger ledger, UserInfo initiator, List<Long> memberIds) {
        boolean isRealUsers = userInfoService.isRealUsers(memberIds);
        org.springframework.util.Assert.isTrue(isRealUsers, "用户不存在");
        Assert.notNull(initiator, "发起人人不存在");
        boolean initiatorHasAuth = ledger.getOwnerId().equals(initiator.getId()) || ledger.getLeaderId().equals(initiator.getId());
        Assert.isTrue(initiatorHasAuth, "账本拥有人和账门人才能邀请");
        boolean isFriends = friendService.isFriends(initiator.getId(), memberIds);
        Assert.isTrue(isFriends, "你们不是朋友");

        NoticeEnums ledgerInvite = NoticeEnums.LEDGER_INVITE;
        String noticeMsg = StringReplacer.build(ledgerInvite.getStrTmpl(), ledger.getName());

        List<Notice> notices = memberIds.stream()
                .map(memberId -> {
                    Notice notice = new Notice();
                    notice.setNoticeType(ledgerInvite);
                    notice.setNoticeName(ledgerInvite.getName());
                    notice.setNoticeMsg(noticeMsg);
                    JSONObject noticeData = new JSONObject();
                    noticeData.put("ledgerId", ledger.getId());
                    notice.setNoticeData(JSON.toJSONString(noticeData));
                    notice.setInitiatorId(initiator.getId());
                    notice.setHandlerId(memberId);
                    return notice;
                })
                .collect(Collectors.toList());

        saveBatch(notices);
    }
}




