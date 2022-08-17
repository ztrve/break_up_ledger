package com.diswares.breakupledger.backend.service.ledger;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.diswares.breakupledger.backend.mapper.LedgerMemberMapper;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.service.friend.FriendService;
import com.diswares.breakupledger.backend.service.notice.NoticeService;
import com.diswares.breakupledger.backend.service.user.UserInfoService;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.vo.ledger.LedgerMemberWalletVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author z_true
 * @description 针对表【ledger_member(账本成员)】的数据库操作Service实现
 * @createDate 2022-07-31 21:23:35
 */
@Service
@RequiredArgsConstructor
public class LedgerMemberServiceImpl extends ServiceImpl<LedgerMemberMapper, LedgerMember>
        implements LedgerMemberService {
    private final NoticeService noticeService;

    private final LedgerMemberMapper ledgerMemberMapper;

    private final UserInfoService userInfoService;

    @Override
    public List<LedgerMemberWalletVo> getMemberWallets(Long ledgerId) {
        UserInfo me = AuthUtil.currentUserInfo();

        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getLedgerId, ledgerId)
                .eq(LedgerMember::getMemberId, me.getId())
                .last("limit 1");
        Assert.isTrue(count(query) > 0, "你不在此账本中");

        query.clear();
        query.eq(LedgerMember::getLedgerId, ledgerId);
        List<LedgerMember> members = list(query);
        List<Long> memberIds = members.stream().map(LedgerMember::getMemberId).collect(Collectors.toList());

        Map<Long, UserInfoVo> userInfoVoMap = userInfoService.listVoByUserIds(memberIds)
                .stream()
                .collect(Collectors.toMap(UserInfoVo::getId, v -> v, (a, b) -> a));

        return members.stream().map(memberPo-> {
            LedgerMemberWalletVo vo = new LedgerMemberWalletVo();
            vo.setId(memberPo.getId());
            vo.setLedgerId(memberPo.getLedgerId());
            vo.setMemberId(memberPo.getMemberId());
            vo.setAmount(memberPo.getWalletAmount());
            vo.setMember(userInfoVoMap.get(memberPo.getMemberId()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public LedgerMemberWalletVo getMemberWallet(Long ledgerId, Long memberId) {
        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getLedgerId, ledgerId)
                .eq(LedgerMember::getMemberId, memberId)
                .last("limit 1");
        LedgerMember ledgerMember = getOne(query);

        UserInfoVo userVo = userInfoService.getOneDetail(memberId);

        LedgerMemberWalletVo vo = new LedgerMemberWalletVo();
        vo.setId(ledgerMember.getId());
        vo.setLedgerId(ledgerMember.getLedgerId());
        vo.setMemberId(ledgerMember.getMemberId());
        vo.setAmount(ledgerMember.getWalletAmount());
        vo.setMember(userVo);
        return vo;
    }

    @Override
    public List<Long> myLedgerIds() {
        UserInfo me = AuthUtil.currentUserInfo();

        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getMemberId, me.getId());
        List<LedgerMember> ledgerMembers = list(query);
        if (ObjectUtils.isEmpty(ledgerMembers)) {
            return null;
        }
        return ledgerMembers.stream()
                .map(LedgerMember::getLedgerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getMemberIdsByLedgerId(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> ledgerMemberQuery = new LambdaQueryWrapper<>();
        ledgerMemberQuery.eq(LedgerMember::getLedgerId, ledgerId);
        return list(ledgerMemberQuery)
                .stream()
                .map(LedgerMember::getMemberId)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerMember> getLedgerMembersByLedgerId(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> ledgerMemberQuery = new LambdaQueryWrapper<>();
        ledgerMemberQuery.eq(LedgerMember::getLedgerId, ledgerId);
        return list(ledgerMemberQuery);
    }

    @Override
    public List<Long> updateLedgerMembers(Ledger ledger, List<Long> memberIds) {
        Assert.notNull(ledger, "账本不存在");
        final Long ledgerId = ledger.getId();
        Assert.isTrue(memberIds.contains(ledger.getOwnerId()), "不可删除拥有人");
        Assert.isTrue(memberIds.contains(ledger.getLeaderId()), "请先修改帐门人");

        LambdaQueryWrapper<LedgerMember> ledgerMemberQuery = new LambdaQueryWrapper<>();
        ledgerMemberQuery.eq(LedgerMember::getLedgerId, ledgerId);
        List<LedgerMember> ledgerMembers = list(ledgerMemberQuery);


        List<LedgerMember> addLedgerMemberList;
        if (!ObjectUtils.isEmpty(ledgerMembers)) {
            List<Long> removeIdList = ledgerMembers.stream()
                    .filter(ledgerMember -> !memberIds.contains(ledgerMember.getMemberId()))
                    .map(LedgerMember::getId)
                    .collect(Collectors.toList());
            if (!ObjectUtils.isEmpty(removeIdList)) {
                removeByIds(removeIdList);
            }
            List<Long> ledgerMemberIds = ledgerMembers.stream().map(LedgerMember::getMemberId).collect(Collectors.toList());
            addLedgerMemberList = memberIds.stream()
                    .filter(memberId -> !ledgerMemberIds.contains(memberId))
                    .map(memberId -> {
                        LedgerMember ledgerMember = new LedgerMember();
                        ledgerMember.setLedgerId(ledgerId);
                        ledgerMember.setMemberId(memberId);
                        return ledgerMember;
                    })
                    .collect(Collectors.toList());
        } else {
            addLedgerMemberList = memberIds.stream()
                    .map(memberId -> {
                        LedgerMember ledgerMember = new LedgerMember();
                        ledgerMember.setLedgerId(ledgerId);
                        ledgerMember.setMemberId(memberId);
                        return ledgerMember;
                    })
                    .collect(Collectors.toList());
        }

        if (!ObjectUtils.isEmpty(addLedgerMemberList)) {
            UserInfo me = AuthUtil.currentUserInfo();
            List<LedgerMember> singleMe = addLedgerMemberList.stream()
                    .filter(addMember -> addMember.getMemberId().equals(me.getId()))
                    .collect(Collectors.toList());
            if (!ObjectUtils.isEmpty(singleMe)) {
                saveBatch(singleMe);
            }
            List<Long> noticeAddMembers = addLedgerMemberList.stream()
                    .map(LedgerMember::getMemberId)
                    .filter(addMemberId -> !addMemberId.equals(me.getId()))
                    .collect(Collectors.toList());
            noticeService.createLedgerInviteNotice(ledger, me, noticeAddMembers);
        }
        return getMemberIdsByLedgerId(ledgerId);
    }

    @Override
    public boolean removeByLedgerId(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getLedgerId, ledgerId);
        return remove(query);
    }

    @Override
    public int getLedgerWalletAmount(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getLedgerId, ledgerId);
        List<LedgerMember> ledgerMembers = list(query);

        if (ObjectUtils.isEmpty(ledgerMembers)) {
            return 0;
        }
        return ledgerMembers.stream()
                .mapToInt(LedgerMember::getWalletAmount)
                .sum();
    }

    @Override
    public Integer getLedgerAmount(Long ledgerId) {
        List<LedgerMember> members = ledgerMemberMapper.getMembers(ledgerId);
        if (ObjectUtils.isEmpty(members)) {
            return 0;
        }
        return members.stream()
                .mapToInt(LedgerMember::getWalletAmount)
                .sum();
    }

}




