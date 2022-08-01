package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.diswares.breakupledger.backend.mapper.LedgerMapper;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.qo.ledger.LedgerCreateQo;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.vo.ledger.LedgerVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author z_true
 * @description 针对表【ledger(账本)】的数据库操作Service实现
 * @createDate 2022-07-31 21:23:35
 */
@Service
@RequiredArgsConstructor
public class LedgerServiceImpl extends ServiceImpl<LedgerMapper, Ledger>
        implements LedgerService {
    private final LedgerMemberService ledgerMemberService;

    private final UserInfoService userInfoService;

    private final NoticeService noticeService;

    @Override
    public List<LedgerVo> myLedgers() {
        List<Long> myLedgerIds = ledgerMemberService.myLedgerIds();
        if (ObjectUtils.isEmpty(myLedgerIds)) {
            return null;
        }
        LambdaQueryWrapper<Ledger> query = new LambdaQueryWrapper<>();
        query.in(Ledger::getId, myLedgerIds);
        List<Ledger> ledgers = list(query);
        if (ObjectUtils.isEmpty(ledgers)) {
            return null;
        }
        return ledgers.stream().map(ledger -> {
            LedgerVo ledgerVo = new LedgerVo();
            BeanUtils.copyProperties(ledger, ledgerVo);
            return ledgerVo;
        }).collect(Collectors.toList());
    }

    @Override
    public LedgerVo getDetailById(Long ledgerId) {
        Ledger ledger = getById(ledgerId);
        if (ObjectUtils.isEmpty(ledger)) {
            return null;
        }

        LedgerVo ledgerVo = new LedgerVo();
        BeanUtils.copyProperties(ledger, ledgerVo);

        // 封装账本owner
        UserInfo owner = userInfoService.getById(ledger.getOwnerId());
        UserInfoVo ownerVo = new UserInfoVo();
        BeanUtils.copyProperties(owner, ownerVo);
        ledgerVo.setOwner(ownerVo);

        // 封装账本leader
        UserInfo leader = userInfoService.getById(ledger.getOwnerId());
        UserInfoVo leaderVo = new UserInfoVo();
        BeanUtils.copyProperties(leader, leaderVo);
        ledgerVo.setLeader(leaderVo);

        // 获取所有账本member
        LambdaQueryWrapper<LedgerMember> ledgerMemberQuery = new LambdaQueryWrapper<>();
        ledgerMemberQuery.eq(LedgerMember::getLedgerId, ledgerId);
        List<Long> memberIds = ledgerMemberService.list(ledgerMemberQuery)
                .stream()
                .map(LedgerMember::getMemberId)
                .collect(Collectors.toList());
        LambdaQueryWrapper<UserInfo> userInfoQuery = new LambdaQueryWrapper<>();
        userInfoQuery.in(UserInfo::getId, memberIds);
        List<UserInfoVo> memberVoList = userInfoService.list(userInfoQuery)
                .stream()
                .map(userInfo -> {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(userInfo, userInfoVo);
                    return userInfoVo;
                })
                .collect(Collectors.toList());
        ledgerVo.setMembers(memberVoList);

        return ledgerVo;
    }

    @Override
    public LedgerVo createLedger(LedgerCreateQo ledgerCreateQo) {
        if (ObjectUtils.isEmpty(ledgerCreateQo.getLedgerMemberIds())) {
            ledgerCreateQo.setLedgerMemberIds(new ArrayList<>());
        }
        UserInfo me = AuthUtil.currentUserInfo();
        if (!ledgerCreateQo.getLedgerMemberIds().contains(me.getId())) {
            ledgerCreateQo.getLedgerMemberIds().add(me.getId());
        }

        Ledger ledger = new Ledger();
        BeanUtils.copyProperties(ledgerCreateQo, ledger);
        ledger.setOwnerId(me.getId());
        ledger.setLeaderId(me.getId());
        save(ledger);

        LedgerMember ledgerMemberOfMe = new LedgerMember();
        ledgerMemberOfMe.setLedgerId(ledger.getId());
        ledgerMemberOfMe.setMemberId(me.getId());
        ledgerMemberService.save(ledgerMemberOfMe);

        // 给 当前用户以外的所有用户 发送通知
        ledgerCreateQo.getLedgerMemberIds().removeIf(id -> id.equals(me.getId()));
        if (!ObjectUtils.isEmpty(ledgerCreateQo.getLedgerMemberIds())) {
            noticeService.createLedgerInviteNotice(ledger, me, ledgerCreateQo.getLedgerMemberIds());
        }
        return getDetailById(ledger.getId());
    }
}




